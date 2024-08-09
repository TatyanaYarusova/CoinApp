package com.example.coinapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coinapp.CoinApp
import com.example.coinapp.R
import com.example.coinapp.databinding.FragmentMainBinding
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.presentation.MainViewModel
import com.example.coinapp.presentation.ViewModelFactory
import com.example.coinapp.presentation.state.ErrorEvent
import com.example.coinapp.presentation.state.ScreenState
import com.example.coinapp.ui.adapter.CoinAdapter
import javax.inject.Inject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var activeCurrency = CURRENCY_USD

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var adapter: CoinAdapter

    private val component by lazy {
        (requireActivity().application as CoinApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        savedInstanceState?.let {
            activeCurrency = it.getString(ACTIVE_CURRENCY_EXTRA) ?: CURRENCY_USD
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ACTIVE_CURRENCY_EXTRA, activeCurrency)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initToolBar()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun initToolBar() {
        if (activeCurrency == CURRENCY_USD) {
            renderActiveButton(binding.toolbar.buttonUsd)
            renderInactiveButton(binding.toolbar.buttonRub)
        } else {
            renderActiveButton(binding.toolbar.buttonRub)
            renderInactiveButton(binding.toolbar.buttonUsd)
        }

        binding.toolbar.buttonUsd.setOnClickListener {
            clickOnUsdButton()
        }

        binding.toolbar.buttonRub.setOnClickListener {
            clickOnRubButton()
        }

    }

    private fun clickOnUsdButton() {
        viewModel.load(CURRENCY_USD)
        activeCurrency = CURRENCY_USD
        renderActiveButton(binding.toolbar.buttonUsd)
        renderInactiveButton(binding.toolbar.buttonRub)
    }

    private fun clickOnRubButton() {
        viewModel.load(CURRENCY_RUB)
        activeCurrency = CURRENCY_RUB
        renderActiveButton(binding.toolbar.buttonRub)
        renderInactiveButton(binding.toolbar.buttonUsd)
    }

    private fun renderActiveButton(button: Button) {
        button.setTextColor(getColor(requireContext(), R.color.active_button_text))
        button.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.active_button_bg)
    }

    private fun renderInactiveButton(button: Button) {
        button.setTextColor(getColor(requireContext(), R.color.inactive_button_text))
        button.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.inactive_button_bg)
    }

    private fun renderState(state: ScreenState<List<Coin>>) {
        when (state) {
            is ScreenState.Content -> renderContent(state.content)
            is ScreenState.Error -> renderError(state.errorType)
            is ScreenState.Loading -> renderLoading()
        }
    }

    private fun renderContent(content: List<Coin>) {
        binding.loadingContainer.root.visibility = View.GONE
        binding.errorContainer.root.visibility = View.GONE

        adapter.onCoinClickListener = object : CoinAdapter.OnCoinClickListener {
            override fun onCoinClick(coin: Coin) {
                addFragment(DetailsFragment.newInstance(coin.id))
            }
        }
        binding.rv.adapter = adapter
        adapter.submitList(content)
        binding.rv.visibility = View.VISIBLE
    }

    private fun renderLoading() {
        binding.loadingContainer.root.visibility = View.VISIBLE
        binding.errorContainer.root.visibility = View.GONE
        binding.rv.visibility = View.GONE
    }

    private fun renderError(errorType: ErrorEvent) {
        binding.errorContainer.root.visibility = View.VISIBLE
        binding.loadingContainer.root.visibility = View.GONE
        binding.rv.visibility = View.GONE

        when (errorType) {
            is ErrorEvent.ServerError -> binding.errorContainer.textError.text =
                getString(R.string.server_error)
            is ErrorEvent.NetworkError -> binding.errorContainer.textError.text =
                getString(R.string.network_error)
        }

        binding.errorContainer.reloadingButton.setOnClickListener {
            viewModel.load(activeCurrency)
        }
    }

    private fun addFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()

        private const val CURRENCY_USD = "USD"
        private const val CURRENCY_RUB = "RUB"
        private const val ACTIVE_CURRENCY_EXTRA = "extra_active_currency"
    }
}
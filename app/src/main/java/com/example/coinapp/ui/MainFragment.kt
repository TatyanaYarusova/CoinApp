package com.example.coinapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
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
        binding.toolbar.buttonUsd.setOnClickListener {

        }

        binding.toolbar.buttonRub.setOnClickListener {

        }

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
        binding.rv.visibility = View.VISIBLE
        binding.rv.adapter = adapter
        adapter.submitList(content)
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
    }
}
package com.example.coinapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coinapp.CoinApp
import com.example.coinapp.databinding.FragmentDetailsBinding
import com.example.coinapp.domain.entity.CoinDetails
import com.example.coinapp.presentation.DetailsViewModel
import com.example.coinapp.presentation.ViewModelFactory
import com.example.coinapp.presentation.state.ErrorEvent
import com.example.coinapp.presentation.state.ScreenState
import com.squareup.picasso.Picasso
import javax.inject.Inject

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private var _coinId: String? = null
    private val coinId get() = _coinId ?: throw RuntimeException("Coin ID is null!")


    private fun parseArguments() {
        val args = requireArguments()
        if (args.containsKey(COIN_ID_EXTRA)) {
            _coinId = args.getString(COIN_ID_EXTRA)
        } else {
            throw RuntimeException("Coin ID argument is absent")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as CoinApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        parseArguments()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDetails(coinId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initToolBar()
    }

    private fun initToolBar() {
        binding.toolbar.title.text = coinId

        binding.toolbar.buttonBack.setOnClickListener {

        }
    }
    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: ScreenState<CoinDetails>) {
        when (state) {
            is ScreenState.Content -> renderContent(state.content)
            is ScreenState.Error -> renderError(state.errorType)
            is ScreenState.Loading -> renderLoading()
        }
    }

    private fun renderContent(content: CoinDetails) {
        Log.d("Tag", content.toString())
        binding.loadingContainer.root.visibility = View.GONE
        binding.errorContainer.root.visibility = View.GONE
        binding.contentContainer.visibility = View.VISIBLE

        with(binding){
            Picasso.get().load(content.img).into(img)
            textDescription.text = content.description
            textCategory.text = content.categories
        }

    }

    private fun renderLoading() {
        binding.loadingContainer.root.visibility = View.VISIBLE
        binding.errorContainer.root.visibility = View.GONE
        binding.contentContainer.visibility = View.GONE
    }

    private fun renderError(errorType: ErrorEvent) {
        binding.errorContainer.root.visibility = View.VISIBLE
        binding.loadingContainer.root.visibility = View.GONE
        binding.contentContainer.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(coinId: String) = DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(COIN_ID_EXTRA, coinId)
                }
            }

        private const val COIN_ID_EXTRA = "extra_coin_id"
    }
}
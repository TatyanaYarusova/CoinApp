package com.example.coinapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coinapp.CoinApp
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
        binding.pb.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE


        val adapter = CoinAdapter()
        adapter.onCoinClickListener = object : CoinAdapter.OnCoinClickListener {
            override fun onCoinClick(coin: Coin) {
                TODO("Not yet implemented")
            }
        }
        binding.rv.visibility = View.VISIBLE
        binding.rv.adapter = adapter
        adapter.submitList(content)
    }

    private fun renderLoading() {
        binding.pb.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
        binding.rv.visibility = View.GONE
    }

    private fun renderError(errorType: ErrorEvent) {
        binding.errorContainer.visibility = View.VISIBLE
        binding.pb.visibility = View.GONE
        binding.rv.visibility = View.GONE
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
package com.example.coinapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinapp.domain.entity.CoinDetails
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.usecase.GetCoinDetailsUseCase
import com.example.coinapp.presentation.state.ErrorEvent
import com.example.coinapp.presentation.state.ScreenState
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val getCoinDetails: GetCoinDetailsUseCase
) : ViewModel()  {

    private val _state: MutableLiveData<ScreenState<CoinDetails>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<CoinDetails>> = _state


    fun getDetails(coinId: String) {
        viewModelScope.launch {
            try {
                when(val coin = getCoinDetails(coinId)) {
                    is RequestResult.Success -> _state.value = ScreenState.Content(coin.content)
                    is RequestResult.Error -> handleError(coin.requestError)
                }
            } catch (e: Exception) {
                _state.value = ScreenState.Error(ErrorEvent.ServerError)
            }
        }
    }

    private fun handleError(errorType: RequestError) {
        _state.value = when (errorType) {
            is RequestError.NetworkError -> ScreenState.Error(ErrorEvent.NetworkError)
            is RequestError.ServerError -> ScreenState.Error(ErrorEvent.ServerError)
        }
    }
}
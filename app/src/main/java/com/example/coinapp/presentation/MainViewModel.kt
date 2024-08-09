package com.example.coinapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.usecase.GetCoinListUseCase
import com.example.coinapp.presentation.state.ErrorEvent
import com.example.coinapp.presentation.state.ScreenState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCoinList: GetCoinListUseCase
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<List<Coin>>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<List<Coin>>> = _state


    init {
        viewModelScope.launch {
            try {
                when(val coinList = getCoinList()) {
                    is RequestResult.Success -> _state.value = ScreenState.Content(coinList.content)
                    is RequestResult.Error -> handleError(coinList.requestError)
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
package com.example.coinapp.presentation.state

sealed class ErrorEvent {

    object ServerError : ErrorEvent()

    object NetworkError: ErrorEvent()

}
package com.example.coinapp.domain.entity.result

sealed class RequestError {

    object ServerError : RequestError()

    object NetworkError: RequestError()

}
package com.example.coinapp.data.repository

import com.example.coinapp.data.api.ApiService
import com.example.coinapp.data.mapper.toEntity
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.repository.CoinListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class CoinListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
): CoinListRepository {

    override suspend fun getCoinList(currency: String): RequestResult<List<Coin>> =
        withContext(ioDispatcher) {
            val response = try {
                apiService.getCoinList(vsCurrency = currency)
            } catch (e: Exception) {
                return@withContext exceptionRequest(e)
            }

            return@withContext if (response.isSuccessful) {
                val coinList = response.body() ?: throw RuntimeException("Response body is null")
                RequestResult.Success(coinList.map { it.toEntity() })
            } else {
                when (val code = response.code()) {
                    in 500..599 -> RequestResult.Error(RequestError.ServerError)
                    else -> throw RuntimeException("Unknown error code: $code")
                }
            }
        }

    private fun <T> exceptionRequest(e: Exception): RequestResult<T> =
        when (e) {
            is UnknownHostException -> RequestResult.Error(RequestError.NetworkError)
            is SocketTimeoutException -> RequestResult.Error(RequestError.NetworkError)
            is SSLHandshakeException -> RequestResult.Error(RequestError.ServerError)
            else -> throw e
        }


}
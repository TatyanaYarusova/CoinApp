package com.example.coinapp.data.repository

import com.example.coinapp.data.api.ApiService
import com.example.coinapp.data.mapper.toEntity
import com.example.coinapp.domain.entity.CoinDetails
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.repository.CoinDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class CoinDetailsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher,
): CoinDetailsRepository {

    override suspend fun getCoinDetails(id: String): RequestResult<CoinDetails> =
        withContext(ioDispatcher) {
            val response = try{
                 apiService.getCoinDetails(id = id)
            } catch (e: Exception) {
                return@withContext exceptionRequest(e)
            }
            return@withContext if (response.isSuccessful) {
                val coinDetails = response.body() ?: throw RuntimeException("Response body is null")
                RequestResult.Success(coinDetails.toEntity())
            } else when (val code = response.code()) {
                in 500..599 -> RequestResult.Error(RequestError.ServerError)
                else -> throw RuntimeException("Unknown error code: $code")
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
package com.example.coinapp.data.api

import com.example.coinapp.data.model.coinDetails.CoinDetailsDto
import com.example.coinapp.data.model.coinList.CoinDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets")
    suspend fun getCoinList(
        @Header(HEADER) key: String = KEY,
        @Query(QUERY_PARAM_VS_CURRENCY) vsCurrency: String,
        @Query(QUERY_PARAM_COUNT) count: Int = COUNT
    ): Response<List<CoinDto>>

    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Header(HEADER) key: String = KEY,
        @Path("id") id: String
    ): Response<CoinDetailsDto>


    companion object {
        private const val QUERY_PARAM_VS_CURRENCY = "vs_currency"
        private const val QUERY_PARAM_COUNT = "per_page"

        private const val COUNT = 20

        private const val HEADER = "x-cg-demo-api-key"
        private const val KEY = "CG-dmDVmGUG9qgxxRbRfHNHGJBo"
    }
}
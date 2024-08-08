package com.example.coinapp.domain.repository

import com.example.coinapp.domain.entity.CoinDetails
import com.example.coinapp.domain.entity.result.RequestResult

interface CoinDetailsRepository {

    suspend fun getCoinDetails(id: String): RequestResult<CoinDetails>

}
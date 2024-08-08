package com.example.coinapp.domain.repository

import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.result.RequestResult

interface CoinListRepository {

    suspend fun getCoinList(currency: String): RequestResult<List<Coin>>

}
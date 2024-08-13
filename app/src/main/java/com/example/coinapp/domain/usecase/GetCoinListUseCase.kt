package com.example.coinapp.domain.usecase

import com.example.coinapp.domain.repository.CoinListRepository
import javax.inject.Inject

class GetCoinListUseCase @Inject constructor(private val repository: CoinListRepository) {
    suspend operator fun invoke(currency: String) = repository.getCoinList(currency)
}
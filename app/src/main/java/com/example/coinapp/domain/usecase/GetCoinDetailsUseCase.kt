package com.example.coinapp.domain.usecase

import com.example.coinapp.domain.repository.CoinDetailsRepository
import javax.inject.Inject

class GetCoinDetailsUseCase @Inject constructor(private val repository: CoinDetailsRepository) {
    suspend operator fun invoke(id: String) = repository.getCoinDetails(id)
}
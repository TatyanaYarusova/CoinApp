package com.example.coinapp.domain

import com.example.coinapp.domain.entity.CoinDetails
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.repository.CoinDetailsRepository
import com.example.coinapp.domain.usecase.GetCoinDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCoinDetailsUseCaseTest {

    private val repository: CoinDetailsRepository = mock()
    private val getCoinDetailsUseCase = GetCoinDetailsUseCase(repository)


    @Test
    fun `invoke returns success result when repository call is successful`() = runTest {
        val coinId = "1"
        val expectedCoinDetails = CoinDetails(
            name = "Bitcoin",
            img = "https://example.com/large.png",
            description = "Bitcoin is a digital currency.",
            categories = "Cryptocurrency, Digital Asset"
        )
        whenever(repository.getCoinDetails(coinId)).thenReturn(RequestResult.Success(expectedCoinDetails))

        val result = getCoinDetailsUseCase(coinId)

        assertTrue(result is RequestResult.Success)
        assertEquals(expectedCoinDetails, (result as RequestResult.Success).content)
    }

    @Test
    fun `invoke returns error result when repository call fails`() = runTest {
        val coinId = "1"
        whenever(repository.getCoinDetails(coinId)).thenReturn(RequestResult.Error(RequestError.ServerError))

        val result = getCoinDetailsUseCase(coinId)

        assertTrue(result is RequestResult.Error)
        assertEquals(RequestError.ServerError, (result as RequestResult.Error).requestError)
    }

    @Test
    fun `invoke returns error result when repository throws an exception`() = runTest {
        val coinId = "1"
        whenever(repository.getCoinDetails(coinId)).thenThrow(RuntimeException("Test exception"))

        val exception = assertThrows<RuntimeException> {
            getCoinDetailsUseCase(coinId)
        }
        assertEquals("Test exception", exception.message)
    }
}
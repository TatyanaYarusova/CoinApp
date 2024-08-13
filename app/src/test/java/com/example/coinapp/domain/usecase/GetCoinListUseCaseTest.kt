package com.example.coinapp.domain.usecase

import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import com.example.coinapp.domain.repository.CoinListRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCoinListUseCaseTest {

    private val repository: CoinListRepository = mock()
    private val getCoinListUseCase = GetCoinListUseCase(repository)


    @Test
    fun `invoke returns success result when repository call is successful`() = runTest {
        val currency = "USD"
        val expectedCoinList = listOf(
            Coin(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                icon = "https://example.com/icon.png",
                price = 45000.0,
                percent = 3.5
            ),
            Coin(
                id = "2",
                name = "Bitcoin",
                symbol = "BTC",
                icon = "https://example.com/icon.png",
                price = 45000.0,
                percent = 3.5
            )
        )
        whenever(repository.getCoinList(currency)).thenReturn(RequestResult.Success(expectedCoinList))

        val result = getCoinListUseCase(currency)

        assertTrue(result is RequestResult.Success)
        assertEquals(expectedCoinList, (result as RequestResult.Success).content)
    }


    @Test
    fun `invoke returns error result when repository call fails`() = runTest {
        val currency = "USD"
        whenever(repository.getCoinList(currency)).thenReturn(RequestResult.Error(RequestError.ServerError))

        val result = getCoinListUseCase(currency)

        assertTrue(result is RequestResult.Error)
        assertEquals(RequestError.ServerError, (result as RequestResult.Error).requestError)
    }

    @Test
    fun `invoke returns error result when repository throws an exception`() = runTest {
        val currency = "USD"
        whenever(repository.getCoinList(currency)).thenThrow(RuntimeException("Test exception"))

        val exception = assertThrows<RuntimeException> {
            getCoinListUseCase(currency)
        }
        assertEquals("Test exception", exception.message)
    }

}
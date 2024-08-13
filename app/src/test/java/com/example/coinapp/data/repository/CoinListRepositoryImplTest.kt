package com.example.coinapp.data.repository

import com.example.coinapp.data.api.ApiService
import com.example.coinapp.data.mapper.toEntity
import com.example.coinapp.data.model.coinList.CoinDto
import com.example.coinapp.domain.entity.result.RequestError
import com.example.coinapp.domain.entity.result.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class CoinListRepositoryImplTest {
    private val apiService: ApiService = mock()
    private val ioDispatcher = Dispatchers.Unconfined
    private val coinListRepository = CoinListRepositoryImpl(apiService, ioDispatcher)

    @Test
    fun `getCoinList returns Success when response is successful`() = runTest {
        val currency = "USD"
        val coinResponse = listOf(
            CoinDto(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                icon = "https://example.com/icon.png",
                price = 45000.0,
                percent = 3.5
            ),
            CoinDto(
                id = "2",
                name = "Bitcoin",
                symbol = "BTC",
                icon = "https://example.com/icon.png",
                price = 45000.0,
                percent = 3.5
            )
        )


        val mockResponse = mock<Response<List<CoinDto>>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn coinResponse
        }

        whenever(apiService.getCoinList(vsCurrency = currency)).thenReturn(mockResponse)

        val result = coinListRepository.getCoinList(currency)

        val expected = coinResponse.map { it.toEntity() }

        assertTrue(result is RequestResult.Success)
        assertEquals(expected, (result as RequestResult.Success).content)
    }

    @Test
    fun `getCoinList returns Error when response returns 5xx code`() = runTest {
        val currency = "USD"

        val mockResponse = mock<Response<List<CoinDto>>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 500
        }
        whenever(apiService.getCoinList(vsCurrency = currency)).thenReturn(mockResponse)

        val result = coinListRepository.getCoinList(currency)

        assertTrue(result is RequestResult.Error)
        assertTrue((result as RequestResult.Error).requestError is RequestError.ServerError)
    }
    @Test
    fun `getCoinList throws exception for unknown error`() = runTest {
        val currency = "USD"

        val mockResponse = mock<Response<List<CoinDto>>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 400
        }
        whenever(apiService.getCoinList(vsCurrency = currency)).thenReturn(mockResponse)

        val exception = assertThrows<RuntimeException> {
            coinListRepository.getCoinList(currency)
        }
        assertEquals("Unknown error code: 400", exception.message)
    }
}
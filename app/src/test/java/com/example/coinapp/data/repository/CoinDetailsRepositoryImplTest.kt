package com.example.coinapp.data.repository

import com.example.coinapp.data.api.ApiService
import com.example.coinapp.data.mapper.toEntity
import com.example.coinapp.data.model.coinDetails.CoinDetailsDto
import com.example.coinapp.data.model.coinDetails.DescriptionDto
import com.example.coinapp.data.model.coinDetails.ImageDto
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

class CoinDetailsRepositoryImplTest {

    private val apiService: ApiService = mock()
    private val ioDispatcher = Dispatchers.Unconfined
    private val coinRepository = CoinDetailsRepositoryImpl(apiService, ioDispatcher)

    @Test
    fun `getCoinDetails returns Success when response is successful`() = runTest {
        val id = "1"

        val imageDto = ImageDto(
            thumb = "https://example.com/thunb.png",
            small = "https://example.com/small.png",
            large = "https://example.com/large.png"
        )
        val descriptionDto = DescriptionDto(en = "Bitcoin is a digital currency.")

        val mockCoinDetailsDto = CoinDetailsDto(
            name = "Bitcoin",
            image = imageDto,
            description = descriptionDto,
            categories = listOf("Cryptocurrency", "Digital Asset")
        )

        val mockResponse = mock<Response<CoinDetailsDto>> {
            on { isSuccessful } doReturn true
            on { body() } doReturn mockCoinDetailsDto
        }

        whenever(apiService.getCoinDetails(id = id)).thenReturn(mockResponse)

        val result = coinRepository.getCoinDetails(id)

        assertTrue(result is RequestResult.Success)
        val successResult = result as RequestResult.Success
        val entity = successResult.content
        assertEquals(mockCoinDetailsDto.toEntity(), entity)
    }

    @Test
    fun `getCoinDetails returns Error when response returns 5xx code`() = runTest {
        val id = "1"
        val mockResponse = mock<Response<CoinDetailsDto>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 500
        }

        whenever(apiService.getCoinDetails(id = id)).thenReturn(mockResponse)

        val result = coinRepository.getCoinDetails(id)

        assertTrue(result is RequestResult.Error)
        val errorResult = result as RequestResult.Error
        assertEquals(RequestError.ServerError, errorResult.requestError)
    }


    @Test
    fun `getCoinDetails throws exception for unknown error`() = runTest {
        val id = "1"
        val mockResponse = mock<Response<CoinDetailsDto>> {
            on { isSuccessful } doReturn false
            on { code() } doReturn 400
        }

        whenever(apiService.getCoinDetails(id = id)).thenReturn(mockResponse)

        val exception = assertThrows<RuntimeException> {
            coinRepository.getCoinDetails(id)
        }
        assertEquals("Unknown error code: 400", exception.message)
    }

}
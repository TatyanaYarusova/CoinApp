package com.example.coinapp.data.mapper

import com.example.coinapp.data.model.coinDetails.CoinDetailsDto
import com.example.coinapp.data.model.coinDetails.DescriptionDto
import com.example.coinapp.data.model.coinDetails.ImageDto
import com.example.coinapp.data.model.coinList.CoinDto
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.CoinDetails
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MappersTest {

    @Test
    fun `test CoinDto to Coin entity conversion`() {
        val coinDto = CoinDto(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            icon = "https://example.com/icon.png",
            price = 45000.0,
            percent = 3.5
        )

        val expectedCoin = Coin(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            icon = "https://example.com/icon.png",
            price = 45000.0,
            percent = 3.5
        )

        val actualCoin = coinDto.toEntity()

        assertEquals(expectedCoin, actualCoin)
    }


    @Test
    fun `test CoinDetailsDto to CoinDetails entity conversion`() {
        val imageDto = ImageDto(
            thumb = "https://example.com/thunb.png",
            small = "https://example.com/small.png",
            large = "https://example.com/large.png"
        )
        val descriptionDto = DescriptionDto(en = "Bitcoin is a digital currency.")

        val coinDetailsDto = CoinDetailsDto(
            name = "Bitcoin",
            image = imageDto,
            description = descriptionDto,
            categories = listOf("Cryptocurrency", "Digital Asset")
        )

        val expectedCoinDetails = CoinDetails(
            name = "Bitcoin",
            img = "https://example.com/large.png",
            description = "Bitcoin is a digital currency.",
            categories = "Cryptocurrency, Digital Asset"
        )

        val actualCoinDetails = coinDetailsDto.toEntity()

        assertEquals(expectedCoinDetails, actualCoinDetails)
    }
}
package com.example.coinapp.data.mapper

import com.example.coinapp.data.model.coinDetails.CoinDetailsDto
import com.example.coinapp.data.model.coinList.CoinDto
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.domain.entity.CoinDetails


fun CoinDto.toEntity() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    icon = icon,
    price = price,
    percent = percent
)

fun CoinDetailsDto.toEntity() = CoinDetails(
    name = name,
    img = image.large,
    description = description.en,
    categories = categories.joinToString(", ")
)
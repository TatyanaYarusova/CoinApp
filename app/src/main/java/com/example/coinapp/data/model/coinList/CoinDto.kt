package com.example.coinapp.data.model.coinList

import com.google.gson.annotations.SerializedName

data class CoinDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("image")
    val icon: String,

    @SerializedName("current_price")
    val price: Double,

    @SerializedName("price_change_percentage_24h")
    val percent: Double
)

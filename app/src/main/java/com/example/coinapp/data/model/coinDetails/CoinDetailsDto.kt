package com.example.coinapp.data.model.coinDetails

import com.google.gson.annotations.SerializedName

data class CoinDetailsDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: DescriptionDto,

    @SerializedName("image")
    val image: ImageDto,

    @SerializedName("categories")
    val categories: List<String>
)

package com.example.coinapp.data.model.coinDetails

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("thumb")
    val thumb: String,

    @SerializedName("small")
    val small: String,

    @SerializedName("large")
    val large: String
)

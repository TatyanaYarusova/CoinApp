package com.example.coinapp.domain.entity

data class Coin (
    val id: String,
    val name: String,
    val symbol: String,
    val icon: String,
    val price: Double,
    val percent: Double
)
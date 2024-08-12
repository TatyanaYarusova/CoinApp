package com.example.coinapp.utils

fun Double.formatNumber(): String {
    val decimalPart = String.format("%.2f", this % 1)
    val wholePart = this.toInt().toString().reversed().chunked(3).joinToString(",").reversed()
    return "$wholePart${decimalPart.substring(decimalPart.indexOf("."))}"
}
package com.example.exchangeratetracker.data.remote.dto

data class ExchangeRateResponse(
    val disclaimer: String?,
    val license: String?,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
package com.example.exchangeratetracker.domain.model

data class CurrencyRate(
    val base: CurrencyInfo,
    val target: CurrencyInfo,
    val rate: Double,
    val isPinned: Boolean
)
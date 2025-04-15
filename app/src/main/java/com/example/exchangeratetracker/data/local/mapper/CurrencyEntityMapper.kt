package com.example.exchangeratetracker.data.local.mapper

import com.example.exchangeratetracker.data.local.entities.CurrencyEntity
import com.example.exchangeratetracker.domain.model.Currency

fun CurrencyEntity.toDomain(): Currency {
    return Currency(code, rate)
}

fun Currency.toEntity(): CurrencyEntity {
    return CurrencyEntity(code = code, rate = rate)
}
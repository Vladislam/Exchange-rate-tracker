package com.example.exchangeratetracker.data.local.mapper

import com.example.exchangeratetracker.data.local.entities.CurrencyRateEntity
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate

fun CurrencyRateEntity.toDomain(
    base: CurrencyInfo,
    target: CurrencyInfo
) = CurrencyRate(base, target, rate)

fun CurrencyRate.toEntity() = CurrencyRateEntity(
    baseCode = base.code,
    targetCode = target.code,
    rate = rate
)
package com.example.exchangeratetracker.data.remote.mapper

import com.example.exchangeratetracker.data.remote.dto.ExchangeRateResponse
import com.example.exchangeratetracker.domain.model.Currency

fun ExchangeRateResponse.toCurrencyList(): List<Currency> {
    return rates.map { Currency(code = it.key, rate = it.value) }
}
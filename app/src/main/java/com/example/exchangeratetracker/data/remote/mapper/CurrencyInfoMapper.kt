package com.example.exchangeratetracker.data.remote.mapper

import com.example.exchangeratetracker.domain.model.CurrencyInfo

fun Map<String, String>.toCurrencyInfoList(): List<CurrencyInfo> {
    return this.map { CurrencyInfo(code = it.key, name = it.value) }
}
package com.example.exchangeratetracker.domain.repository

import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getAvailableCurrencies(): Resource<List<CurrencyInfo>>
    suspend fun getExchangeRatesForSymbols(symbols: List<String>): Resource<List<CurrencyRate>>
    fun observePinnedRates(): Flow<List<CurrencyRate>>
    suspend fun pinCurrency(code: String)
    suspend fun unpinCurrency(code: String)
}
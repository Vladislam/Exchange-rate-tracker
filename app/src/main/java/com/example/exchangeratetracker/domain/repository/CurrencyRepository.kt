package com.example.exchangeratetracker.domain.repository

import com.example.exchangeratetracker.domain.model.Currency
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getLatestRates(base: String): List<Currency>
    fun observeSavedCurrencies(): Flow<List<Currency>>
    suspend fun removeCurrency(code: String)
    suspend fun pinCurrency(code: String)
    suspend fun unpinCurrency(code: String)
    suspend fun getAvailableCurrencies(): Resource<List<CurrencyInfo>>
    suspend fun getExchangeRatesForSymbols(symbols: List<String>): Resource<List<CurrencyRate>>
    suspend fun getCachedRatesForBase(base: CurrencyInfo): List<CurrencyRate>
}
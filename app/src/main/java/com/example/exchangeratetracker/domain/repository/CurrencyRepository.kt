package com.example.exchangeratetracker.domain.repository

import com.example.exchangeratetracker.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getLatestRates(base: String): List<Currency>
    fun observeSavedCurrencies(): Flow<List<Currency>>
    suspend fun removeCurrency(code: String)
}
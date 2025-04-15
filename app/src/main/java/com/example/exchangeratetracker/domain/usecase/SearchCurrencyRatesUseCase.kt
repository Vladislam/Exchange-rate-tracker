package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository,
    private val getAvailableCurrencies: GetAvailableCurrenciesUseCase
) {
    fun observe(query: String): Flow<List<CurrencyRate>> {
        val base = "USD"
        return repository.observeSearchRates(base, query)
    }

    suspend fun refresh(query: String): Resource<Unit> {
        val allCurrencies = when (val result = getAvailableCurrencies()) {
            is Resource.Success -> result.data
            is Resource.Error -> return Resource.Error("Failed to load currencies")
        }

        val matched = allCurrencies.filter {
            it.code.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }

        if (matched.isEmpty()) return Resource.Success(Unit)

        val symbols = matched.map { it.code }

        return when (repository.getExchangeRatesForSymbols(symbols)) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Error -> Resource.Error("Failed to load exchange rates")
        }
    }
}
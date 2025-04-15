package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.utils.Resource
import javax.inject.Inject

class SearchCurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository,
    private val getAvailableCurrencies: GetAvailableCurrenciesUseCase
) {
    suspend operator fun invoke(query: String): Resource<List<CurrencyRate>> {
        val allCurrencies = when (val result = getAvailableCurrencies()) {
            is Resource.Success -> result.data
            is Resource.Error -> return Resource.Error("Failed to load currencies")
        }

        val base = CurrencyInfo("USD", "United States Dollar")

        val matched = allCurrencies.filter {
            it.code.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
        }

        if (matched.isEmpty()) return Resource.Success(emptyList())

        val symbols = matched.map { it.code }

        return when (val result = repository.getExchangeRatesForSymbols(symbols)) {
            is Resource.Success -> {
                val filtered = result.data.filter { it.base.code == base.code }
                Resource.Success(filtered)
            }

            is Resource.Error -> Resource.Error(result.message)
        }
    }
}
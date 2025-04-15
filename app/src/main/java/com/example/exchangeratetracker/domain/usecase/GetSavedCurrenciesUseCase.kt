package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.model.Currency
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    operator fun invoke(): Flow<List<Currency>> {
        return repository.observeSavedCurrencies()
    }
}
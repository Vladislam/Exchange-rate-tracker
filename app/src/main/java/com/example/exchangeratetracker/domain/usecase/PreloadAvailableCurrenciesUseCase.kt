package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import javax.inject.Inject

class PreloadAvailableCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke() {
        repository.getAvailableCurrencies() // already cached if exists
    }
}
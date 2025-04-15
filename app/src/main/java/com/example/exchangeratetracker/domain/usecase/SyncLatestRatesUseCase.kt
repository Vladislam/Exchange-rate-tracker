package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import javax.inject.Inject

class SyncLatestRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(base: String) {
        repository.getLatestRates(base)
    }
}
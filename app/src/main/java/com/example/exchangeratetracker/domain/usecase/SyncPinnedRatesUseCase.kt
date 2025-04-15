package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncPinnedRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository,
    private val observePinnedRates: ObservePinnedRatesUseCase
) {
    suspend operator fun invoke() {
        val pinned = observePinnedRates().first()
        if (pinned.isNotEmpty()) {
            repository.getExchangeRatesForSymbols(pinned.map { it.target.code })
        }
    }
}

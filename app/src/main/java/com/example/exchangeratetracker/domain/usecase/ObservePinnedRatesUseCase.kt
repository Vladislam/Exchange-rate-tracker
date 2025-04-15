package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePinnedRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    operator fun invoke(): Flow<List<CurrencyRate>> {
        return repository.observePinnedRates()
    }
}
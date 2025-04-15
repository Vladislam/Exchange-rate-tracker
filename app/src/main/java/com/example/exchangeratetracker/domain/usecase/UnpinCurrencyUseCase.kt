package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import javax.inject.Inject

class UnpinCurrencyUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(code: String) {
        repository.unpinCurrency(code)
    }
}
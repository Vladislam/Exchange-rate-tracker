package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.utils.Resource
import javax.inject.Inject

class GetAvailableCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Resource<List<CurrencyInfo>> {
        return repository.getAvailableCurrencies()
    }
}
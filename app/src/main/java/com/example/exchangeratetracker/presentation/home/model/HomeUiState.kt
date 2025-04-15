package com.example.exchangeratetracker.presentation.home.model

import com.example.exchangeratetracker.domain.model.CurrencyRate

data class HomeUiState(
    val isLoading: Boolean = false,
    val rates: List<CurrencyRate> = emptyList()
)
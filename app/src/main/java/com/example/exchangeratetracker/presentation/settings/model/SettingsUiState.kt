package com.example.exchangeratetracker.presentation.settings.model

import com.example.exchangeratetracker.domain.model.CurrencyInfo

data class SettingsUiState(
    val selectedBase: CurrencyInfo = CurrencyInfo("USD", "United States Dollar"),
    val availableCurrencies: List<CurrencyInfo> = emptyList()
)
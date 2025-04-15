package com.example.exchangeratetracker.presentation.settings.model

interface SettingsIntent {
    fun onBaseCurrencyChange(code: String)
}
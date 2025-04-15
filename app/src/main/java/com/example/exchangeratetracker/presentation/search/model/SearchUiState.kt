package com.example.exchangeratetracker.presentation.search.model

import com.example.exchangeratetracker.domain.model.CurrencyRate

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<CurrencyRate> = emptyList()
)
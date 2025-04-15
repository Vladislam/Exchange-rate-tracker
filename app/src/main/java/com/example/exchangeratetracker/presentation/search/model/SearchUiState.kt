package com.example.exchangeratetracker.presentation.search.model

import com.example.exchangeratetracker.domain.model.CurrencyInfo

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val items: List<CurrencyInfo> = emptyList(),
)
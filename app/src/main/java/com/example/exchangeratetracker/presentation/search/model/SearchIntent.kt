package com.example.exchangeratetracker.presentation.search.model

interface SearchIntent {
    fun onQueryChange(query: String)
    fun onClearQuery()
    fun onCurrencyClick(code: String)
}
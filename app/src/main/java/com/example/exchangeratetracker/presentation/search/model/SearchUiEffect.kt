package com.example.exchangeratetracker.presentation.search.model

sealed interface SearchUiEffect {
    data class ShowError(val message: String) : SearchUiEffect
}
package com.example.exchangeratetracker.presentation.home.model

sealed interface HomeUiEffect {
    data class ShowError(val message: String) : HomeUiEffect
}
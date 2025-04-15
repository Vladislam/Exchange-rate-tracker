package com.example.exchangeratetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.domain.usecase.PreloadAvailableCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val preloadCurrenciesUseCase: PreloadAvailableCurrenciesUseCase
) : ViewModel() {

    init {
        preloadCurrencies()
    }

    private fun preloadCurrencies() {
        viewModelScope.launch {
            preloadCurrenciesUseCase()
        }
    }
}
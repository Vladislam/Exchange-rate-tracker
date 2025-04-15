package com.example.exchangeratetracker.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.data.local.preferences.BaseCurrencyPreferences
import com.example.exchangeratetracker.domain.usecase.GetAvailableCurrenciesUseCase
import com.example.exchangeratetracker.presentation.settings.model.SettingsIntent
import com.example.exchangeratetracker.presentation.settings.model.SettingsUiState
import com.example.exchangeratetracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: BaseCurrencyPreferences,
    private val getAvailableCurrencies: GetAvailableCurrenciesUseCase
) : ViewModel(), SettingsIntent {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            val available = when (val result = getAvailableCurrencies()) {
                is Resource.Success -> result.data
                is Resource.Error -> emptyList()
            }

            prefs.getBaseCurrency(available).collectLatest { baseCurrency ->
                _uiState.value = SettingsUiState(
                    selectedBase = baseCurrency,
                    availableCurrencies = available
                )
            }
        }
    }

    override fun onBaseCurrencyChange(code: String) {
        viewModelScope.launch {
            prefs.setBaseCurrency(code)
        }
    }
}
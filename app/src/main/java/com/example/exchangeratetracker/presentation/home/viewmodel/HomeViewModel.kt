package com.example.exchangeratetracker.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.domain.usecase.ObservePinnedRatesUseCase
import com.example.exchangeratetracker.domain.usecase.PinCurrencyUseCase
import com.example.exchangeratetracker.domain.usecase.UnpinCurrencyUseCase
import com.example.exchangeratetracker.presentation.home.model.HomeIntent
import com.example.exchangeratetracker.presentation.home.model.HomeUiEffect
import com.example.exchangeratetracker.presentation.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val unpinCurrencyUseCase: UnpinCurrencyUseCase,
    private val pinCurrencyUseCase: PinCurrencyUseCase,
    private val observePinnedRatesUseCase: ObservePinnedRatesUseCase,
) : ViewModel(), HomeIntent {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeRates()
    }

    private fun observeRates() {
        observePinnedRatesUseCase()
            .onEach { rates ->
                _uiState.update {
                    it.copy(rates = rates, isLoading = false)
                }
            }
            .catch {
                _uiEffect.send(HomeUiEffect.ShowError("Failed to load pinned currencies."))
            }
            .launchIn(viewModelScope)
    }

    override fun onUnpinCurrency(code: String) {
        viewModelScope.launch {
            unpinCurrencyUseCase(code)
        }
    }

    override fun onPinCurrency(code: String) {
        viewModelScope.launch {
            pinCurrencyUseCase(code)
        }
    }
}
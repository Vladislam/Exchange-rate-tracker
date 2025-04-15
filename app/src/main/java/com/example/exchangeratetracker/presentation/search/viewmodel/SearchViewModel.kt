package com.example.exchangeratetracker.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.domain.usecase.PinCurrencyUseCase
import com.example.exchangeratetracker.domain.usecase.SearchCurrencyRatesUseCase
import com.example.exchangeratetracker.presentation.search.model.SearchIntent
import com.example.exchangeratetracker.presentation.search.model.SearchUiEffect
import com.example.exchangeratetracker.presentation.search.model.SearchUiState
import com.example.exchangeratetracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pinCurrency: PinCurrencyUseCase,
    private val searchUseCase: SearchCurrencyRatesUseCase
) : ViewModel(), SearchIntent {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<SearchUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var debounceJob: Job? = null

    override fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(400L)
            updateLoading(true)

            when (val result = searchUseCase(query)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(results = result.data)
                    }
                }

                is Resource.Error -> {
                    _uiEffect.send(SearchUiEffect.ShowError(result.message))
                }
            }
            updateLoading(false)
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    override fun onClearQuery() {
        _uiState.update { it.copy(query = "", results = emptyList()) }
    }

    override fun onCurrencyClick(code: String) {
        viewModelScope.launch {
            pinCurrency(code)
        }
    }
}

package com.example.exchangeratetracker.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.domain.usecase.PinCurrencyUseCase
import com.example.exchangeratetracker.presentation.search.model.SearchIntent
import com.example.exchangeratetracker.presentation.search.model.SearchUiEffect
import com.example.exchangeratetracker.presentation.search.model.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pinCurrency: PinCurrencyUseCase
) : ViewModel(), SearchIntent {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<SearchUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    override fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    override fun onClearQuery() {
        _uiState.update { it.copy(query = "") }
    }

    override fun onCurrencyClick(code: String) {
        viewModelScope.launch {
            pinCurrency(code)
        }
    }
}
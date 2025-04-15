package com.example.exchangeratetracker.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeratetracker.domain.usecase.AddRecentSearchUseCase
import com.example.exchangeratetracker.domain.usecase.GetRecentSearchesUseCase
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pinCurrency: PinCurrencyUseCase,
    private val searchUseCase: SearchCurrencyRatesUseCase,
    private val getRecentSearches: GetRecentSearchesUseCase,
    private val addRecentSearch: AddRecentSearchUseCase
) : ViewModel(), SearchIntent {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<SearchUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            getRecentSearches().collectLatest { recentSearches ->
                _uiState.update {
                    it.copy(recentSearches = recentSearches)
                }
            }
        }
    }

    override fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400L)
            observeLocal(query)
            refreshRemote(query)
        }
    }

    private fun observeLocal(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchUseCase.observe(query).collectLatest { rates ->
                _uiState.update { it.copy(results = rates) }
            }
        }
    }

    private fun refreshRemote(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = searchUseCase.refresh(query)
            if (result is Resource.Error) {
                _uiEffect.send(SearchUiEffect.ShowError(result.message))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    override fun onClearQuery() {
        _uiState.update { it.copy(query = "", results = emptyList()) }
        searchJob?.cancel()
    }

    override fun onCurrencyClick(code: String) {
        viewModelScope.launch {
            pinCurrency(code)
            addRecentSearch(code)
            _uiState.update { current ->
                val updated = current.results.map {
                    if (it.target.code == code) it.copy(isPinned = true) else it
                }
                current.copy(results = updated)
            }
        }
    }
}

package com.example.exchangeratetracker.presentation.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.exchangeratetracker.presentation.search.component.SearchCurrencyItem
import com.example.exchangeratetracker.presentation.search.model.SearchIntent
import com.example.exchangeratetracker.presentation.search.model.SearchUiEffect
import com.example.exchangeratetracker.presentation.search.model.SearchUiState
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchScreen(uiState: SearchUiState, uiEffect: Flow<SearchUiEffect>, intent: SearchIntent) {

    HandleUiEffect(uiEffect)

    SearchBody(uiState, intent)
}

@Composable
fun HandleUiEffect(uiEffect: Flow<SearchUiEffect>) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is SearchUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun SearchBody(
    uiState: SearchUiState,
    intent: SearchIntent
) {
    if (uiState.isLoading) {
        CircularProgressIndicator()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.query,
            onValueChange = intent::onQueryChange,
            label = { Text("Search currencies...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                    IconButton(onClick = intent::onClearQuery) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            }
        )
        val filtered = if (uiState.query.isBlank()) uiState.items.take(15)
        else uiState.items.filter {
            it.code.contains(uiState.query, ignoreCase = true) || it.name.contains(
                uiState.query,
                ignoreCase = true
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filtered) { currency ->
                SearchCurrencyItem(
                    code = currency.code,
                    name = currency.name,
                    onClick = { intent.onCurrencyClick(currency.code) }
                )
            }
        }
    }
}
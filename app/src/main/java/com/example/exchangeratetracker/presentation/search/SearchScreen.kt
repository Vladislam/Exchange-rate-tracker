package com.example.exchangeratetracker.presentation.search

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        LazyColumn {
            items(uiState.results) { rate ->
                SearchCurrencyItem(rate = rate) {
                    intent.onCurrencyClick(rate.target.code)
                }
            }
        }
    }
}
package com.example.exchangeratetracker.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
            if (effect is SearchUiEffect.ShowError) {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun SearchBody(
    uiState: SearchUiState,
    intent: SearchIntent
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = intent::onQueryChange,
                label = { Text("Search currencies") },
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = intent::onClearQuery) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.query.isBlank()) {
                if (uiState.recentSearches.isEmpty()) {
                    Text(
                        text = "There's no recent searches",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        items(uiState.recentSearches) { code ->
                            AssistChip(
                                onClick = { intent.onQueryChange(code) },
                                label = { Text(code) }
                            )
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.results) { rate ->
                        SearchCurrencyItem(rate = rate) {
                            intent.onCurrencyClick(rate.target.code)
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Dialog(onDismissRequest = {}) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
package com.example.exchangeratetracker.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exchangeratetracker.presentation.settings.model.SettingsIntent
import com.example.exchangeratetracker.presentation.settings.model.SettingsUiState

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    intent: SettingsIntent
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Base Currency", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        val expanded = remember { mutableStateOf(false) }

        Box {
            OutlinedTextField(
                value = "${uiState.selectedBase.name} (${uiState.selectedBase.code})",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select base currency") },
                trailingIcon = {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                uiState.availableCurrencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text("${currency.name} (${currency.code})") },
                        onClick = {
                            intent.onBaseCurrencyChange(currency.code)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}
package com.example.exchangeratetracker.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.exchangeratetracker.presentation.settings.model.SettingsIntent
import com.example.exchangeratetracker.presentation.settings.model.SettingsUiState

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    intent: SettingsIntent
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp)
    ) {
        Text("Base Currency", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = "${uiState.selectedBase.name} (${uiState.selectedBase.code})",
            onValueChange = {},
            label = { Text("Base currency (locked)") },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .clickable {
                    Toast
                        .makeText(
                            context,
                            "Base currency selection is only available in paid plans.",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                },
            readOnly = true,
            enabled = false
        )
    }
}
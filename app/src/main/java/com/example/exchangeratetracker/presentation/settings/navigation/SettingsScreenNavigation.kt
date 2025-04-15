package com.example.exchangeratetracker.presentation.settings.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exchangeratetracker.presentation.navigation.BottomNavItem
import com.example.exchangeratetracker.presentation.settings.SettingsScreen
import com.example.exchangeratetracker.presentation.settings.viewModel.SettingsViewModel

fun NavGraphBuilder.settingsNavigation() {
    composable(
        route = BottomNavItem.Settings.route
    ) {
        val viewModel = hiltViewModel<SettingsViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        SettingsScreen(
            uiState = state,
            intent = viewModel
        )
    }
}

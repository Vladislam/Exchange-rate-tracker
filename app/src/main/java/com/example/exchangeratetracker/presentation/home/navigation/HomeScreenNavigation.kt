package com.example.exchangeratetracker.presentation.home.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exchangeratetracker.presentation.home.HomeScreen
import com.example.exchangeratetracker.presentation.home.viewmodel.HomeViewModel
import com.example.exchangeratetracker.presentation.navigation.BottomNavItem

fun NavGraphBuilder.homeNavigation() {
    composable(
        route = BottomNavItem.Home.route
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        HomeScreen(
            uiState = state,
            uiEffect = viewModel.uiEffect,
            intent = viewModel
        )
    }
}

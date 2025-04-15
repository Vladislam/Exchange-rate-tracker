package com.example.exchangeratetracker.presentation.search.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exchangeratetracker.presentation.navigation.BottomNavItem
import com.example.exchangeratetracker.presentation.search.SearchScreen
import com.example.exchangeratetracker.presentation.search.viewmodel.SearchViewModel

fun NavGraphBuilder.searchNavigation() {
    composable(
        route = BottomNavItem.Search.route
    ) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        SearchScreen(
            uiState = state,
            uiEffect = viewModel.uiEffect,
            intent = viewModel
        )
    }
}

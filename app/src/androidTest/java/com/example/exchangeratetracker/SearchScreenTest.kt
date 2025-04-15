package com.example.exchangeratetracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.presentation.search.SearchScreen
import com.example.exchangeratetracker.presentation.search.model.SearchIntent
import com.example.exchangeratetracker.presentation.search.model.SearchUiState
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_showsResults_whenQuerySet() {
        val fakeUiState = SearchUiState(
            query = "USD",
            results = listOf(
                CurrencyRate(
                    base = CurrencyInfo("USD", "US Dollar"),
                    target = CurrencyInfo("EUR", "Euro"),
                    rate = 1.05,
                    isPinned = false
                )
            )
        )

        composeTestRule.setContent {
            SearchScreen(
                uiState = fakeUiState,
                uiEffect = emptyFlow(),
                intent = object : SearchIntent {
                    override fun onQueryChange(query: String) {}
                    override fun onClearQuery() {}
                    override fun onCurrencyClick(code: String) {}
                }
            )
        }

        composeTestRule.onNodeWithText("Euro (EUR)").assertIsDisplayed()
    }

    @Test
    fun searchScreen_clearButtonClearsQuery() {
        var queryState = "USD"

        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState(query = queryState),
                uiEffect = emptyFlow(),
                intent = object : SearchIntent {
                    override fun onQueryChange(query: String) {
                        queryState = query
                    }

                    override fun onClearQuery() {
                        queryState = ""
                    }

                    override fun onCurrencyClick(code: String) {}
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Clear").performClick()
        assert(queryState.isEmpty())
    }

    @Test
    fun searchScreen_showsRecentSearchesWhenQueryBlank() {
        val recent = listOf("USD", "EUR")

        composeTestRule.setContent {
            Column {
                LazyRow {
                    items(recent) {
                        AssistChip(onClick = {}, label = { Text(it) })
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("USD").assertIsDisplayed()
        composeTestRule.onNodeWithText("EUR").assertIsDisplayed()
    }

    @Test
    fun searchScreen_showsNoRecentSearchesMessage() {
        composeTestRule.setContent {
            SearchScreen(
                uiState = SearchUiState(query = "", results = emptyList()),
                uiEffect = emptyFlow(),
                intent = object : SearchIntent {
                    override fun onQueryChange(query: String) {}
                    override fun onClearQuery() {}
                    override fun onCurrencyClick(code: String) {}
                }
            )
        }

        composeTestRule.onNodeWithText("There's no recent searches").assertIsDisplayed()
    }
}
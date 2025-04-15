package com.example.exchangeratetracker

import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.domain.usecase.AddRecentSearchUseCase
import com.example.exchangeratetracker.domain.usecase.GetRecentSearchesUseCase
import com.example.exchangeratetracker.domain.usecase.PinCurrencyUseCase
import com.example.exchangeratetracker.domain.usecase.SearchCurrencyRatesUseCase
import com.example.exchangeratetracker.presentation.search.model.SearchUiEffect
import com.example.exchangeratetracker.presentation.search.viewmodel.SearchViewModel
import com.example.exchangeratetracker.utils.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val pinCurrency = mockk<PinCurrencyUseCase>(relaxed = true)
    private val searchUseCase = mockk<SearchCurrencyRatesUseCase>(relaxed = true)
    private val getRecent = mockk<GetRecentSearchesUseCase>(relaxed = true)
    private val addRecent = mockk<AddRecentSearchUseCase>(relaxed = true)

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val dispatcherRule = MainDispatcherRule()


    @Before
    fun setup() {
        viewModel = SearchViewModel(pinCurrency, searchUseCase, getRecent, addRecent)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCurrencyClick should mark rate as pinned`() = runTest {
        coEvery { searchUseCase.observe("eur") } returns flowOf(
            listOf(
                CurrencyRate(
                    CurrencyInfo("USD", "US Dollar"),
                    CurrencyInfo("EUR", "Euro"),
                    1.1,
                    false
                )
            )
        )
        coEvery { searchUseCase.refresh("eur") } returns Resource.Success(Unit)
        viewModel.onQueryChange("eur")
        advanceUntilIdle()

        viewModel.onCurrencyClick("EUR")
        val updated = viewModel.uiState.value.results.first { it.target.code == "EUR" }
        assertTrue(updated.isPinned)
    }

    @Test
    fun `onClearQuery clears the query and results`() = runTest {
        viewModel.onQueryChange("usd")
        delay(500)
        viewModel.onClearQuery()

        val state = viewModel.uiState.value
        assertEquals("", state.query)
        assertTrue(state.results.isEmpty())
    }

    @Test
    fun `onQueryChange emits error when refresh fails`() = runTest {
        coEvery { searchUseCase.refresh(any()) } returns Resource.Error("Something broke")
        coEvery { searchUseCase.observe(any()) } returns flowOf(emptyList())

        viewModel.onQueryChange("fail")

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is SearchUiEffect.ShowError)
    }

    @Test
    fun `onCurrencyClick adds recent search`() = runTest {
        coEvery { addRecent(any()) } just Runs

        viewModel.onCurrencyClick("JPY")

        coVerify { addRecent("JPY") }
    }

    @Test
    fun `onQueryChange updates query in state`() = runTest {
        viewModel.onQueryChange("eur")
        assertEquals("eur", viewModel.uiState.value.query)
    }
}
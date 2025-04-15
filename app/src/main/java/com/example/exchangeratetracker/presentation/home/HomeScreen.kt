package com.example.exchangeratetracker.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.exchangeratetracker.presentation.home.component.CurrencyCard
import com.example.exchangeratetracker.presentation.home.model.HomeIntent
import com.example.exchangeratetracker.presentation.home.model.HomeUiEffect
import com.example.exchangeratetracker.presentation.home.model.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    intent: HomeIntent,
    uiEffect: Flow<HomeUiEffect>,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiEffect(uiEffect)
    HomeBody(uiState, intent, snackbarHostState)
}

@Composable
fun HandleUiEffect(uiEffect: Flow<HomeUiEffect>) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeBody(
    uiState: HomeUiState,
    intent: HomeIntent,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.rates.isEmpty() -> {
                Text(
                    text = "\uD83D\uDD16 No pinned currencies yet.\nSearch and pin the ones you care about.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.rates, key = { it.target.code }) { rate ->
                        var isVisible by remember { mutableStateOf(true) }

                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                    isVisible = false
                                    scope.launch {
                                        delay(300)
                                        intent.onUnpinCurrency(rate.target.code)
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Removed ${rate.target.code}",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            intent.onPinCurrency(rate.target.code)
                                        }
                                    }
                                    true
                                } else false
                            }
                        )

                        SwipeToDismiss(
                            modifier = Modifier.animateItem(),
                            state = dismissState,
                            background = {
                                val alignment = when (dismissState.dismissDirection) {
                                    DismissDirection.EndToStart -> Alignment.CenterEnd
                                    else -> Alignment.CenterStart
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.errorContainer)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.graphicsLayer {
                                            alpha = dismissState.progress.fraction
                                            scaleX = 0.8f + (0.2f * dismissState.progress.fraction)
                                            scaleY = scaleX
                                        }
                                    )
                                }
                            },
                            dismissContent = {
                                CurrencyCard(rate)
                            },
                            directions = setOf(DismissDirection.EndToStart)
                        )
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
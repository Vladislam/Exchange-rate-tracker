package com.example.exchangeratetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ExchangeRateTrackerTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = PrimaryBlue,
        background = Background,
        surface = Surface,
        error = ErrorRed,
        onPrimary = OnPrimary,
        onSurface = OnSurface
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
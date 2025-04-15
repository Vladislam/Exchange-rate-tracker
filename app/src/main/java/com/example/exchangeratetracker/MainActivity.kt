package com.example.exchangeratetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.exchangeratetracker.manager.CurrencySyncStarter
import com.example.exchangeratetracker.presentation.navigation.Navigation
import com.example.exchangeratetracker.ui.theme.ExchangeRateTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CurrencySyncStarter.start(this)
        setContent {
            ExchangeRateTrackerTheme {
                Navigation()
            }
        }
    }
}
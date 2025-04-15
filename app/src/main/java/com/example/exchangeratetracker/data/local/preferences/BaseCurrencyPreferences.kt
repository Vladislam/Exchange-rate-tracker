package com.example.exchangeratetracker.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseCurrencyPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    private object Keys {
        val BASE_CURRENCY = stringPreferencesKey("base_currency")
    }

    val baseCurrency: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[Keys.BASE_CURRENCY] ?: "USD" }

    suspend fun setBaseCurrency(code: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.BASE_CURRENCY] = code
        }
    }

    fun getBaseCurrency(allCurrencies: List<CurrencyInfo>): Flow<CurrencyInfo> {
        return context.dataStore.data.map { prefs ->
            val code = prefs[Keys.BASE_CURRENCY] ?: "USD"
            allCurrencies.find { it.code == code }
                ?: CurrencyInfo(code = "USD", name = "United States Dollar")
        }
    }
}

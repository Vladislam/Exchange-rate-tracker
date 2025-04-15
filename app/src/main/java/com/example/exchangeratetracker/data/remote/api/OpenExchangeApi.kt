package com.example.exchangeratetracker.data.remote.api

import com.example.exchangeratetracker.BuildConfig
import com.example.exchangeratetracker.data.remote.dto.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenExchangeApi {

    @GET("latest.json")
    suspend fun getLatestRates(
        @Query("app_id") appId: String = BuildConfig.OPEN_EXCHANGE_API_KEY,
        @Query("symbols") symbols: String? = null,
    ): ExchangeRateResponse

    @GET("currencies.json")
    suspend fun getAvailableCurrencies(): Map<String, String>
}
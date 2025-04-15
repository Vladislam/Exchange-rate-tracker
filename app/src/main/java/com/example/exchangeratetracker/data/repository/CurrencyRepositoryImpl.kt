package com.example.exchangeratetracker.data.repository

import com.example.exchangeratetracker.BuildConfig
import com.example.exchangeratetracker.data.local.dao.CurrencyDao
import com.example.exchangeratetracker.data.local.mapper.toDomain
import com.example.exchangeratetracker.data.local.mapper.toEntity
import com.example.exchangeratetracker.data.remote.api.OpenExchangeApi
import com.example.exchangeratetracker.data.remote.mapper.toCurrencyList
import com.example.exchangeratetracker.domain.model.Currency
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: OpenExchangeApi,
    private val dao: CurrencyDao
) : CurrencyRepository {

    override suspend fun getLatestRates(base: String): List<Currency> {
        return try {
            val response = api.getLatestRates(
                appId = BuildConfig.OPEN_EXCHANGE_API_KEY,
                base = base,
            )

            val currencies = response.toCurrencyList()

            val currencyEntities = currencies.map { it.toEntity() }
            dao.insertAll(currencyEntities)

            currencies
        } catch (e: Exception) {
            dao.getPinnedCurrencies().map { it.toDomain() }
        }
    }

    override fun observeSavedCurrencies(): Flow<List<Currency>> {
        return dao.observePinnedCurrencies()
            .map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun removeCurrency(code: String) {
        dao.deleteByCode(code)
    }
}
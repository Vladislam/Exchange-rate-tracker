package com.example.exchangeratetracker.data.repository

import com.example.exchangeratetracker.BuildConfig
import com.example.exchangeratetracker.data.local.dao.CurrencyDao
import com.example.exchangeratetracker.data.local.dao.CurrencyInfoDao
import com.example.exchangeratetracker.data.local.mapper.toDomain
import com.example.exchangeratetracker.data.local.mapper.toEntity
import com.example.exchangeratetracker.data.remote.api.OpenExchangeApi
import com.example.exchangeratetracker.data.remote.mapper.toCurrencyInfoList
import com.example.exchangeratetracker.data.remote.mapper.toCurrencyList
import com.example.exchangeratetracker.domain.model.Currency
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: OpenExchangeApi,
    private val currencyDao: CurrencyDao,
    private val currencyInfoDao: CurrencyInfoDao,
) : CurrencyRepository {

    override suspend fun getLatestRates(base: String): List<Currency> {
        return try {
            val response = api.getLatestRates(
                appId = BuildConfig.OPEN_EXCHANGE_API_KEY,
                base = base,
            )

            val currencies = response.toCurrencyList()

            val currencyEntities = currencies.map { it.toEntity() }
            currencyDao.insertAll(currencyEntities)

            currencies
        } catch (e: Exception) {
            currencyDao.getPinnedCurrencies().map { it.toDomain() }
        }
    }

    override fun observeSavedCurrencies(): Flow<List<Currency>> {
        return currencyDao.observePinnedCurrencies().map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun removeCurrency(code: String) {
        currencyDao.deleteByCode(code)
    }

    override suspend fun pinCurrency(code: String) {
        currencyDao.pin(code)
    }

    override suspend fun unpinCurrency(code: String) {
        currencyDao.unpin(code)
    }

    override suspend fun getAvailableCurrencies(): Resource<List<CurrencyInfo>> {
        val local = currencyInfoDao.getAll()

        return if (local.isNotEmpty()) {
            Resource.Success(local.map { it.toDomain() })
        } else {
            return try {
                val remote = api.getAvailableCurrencies().toCurrencyInfoList()
                currencyInfoDao.insertAll(remote.map { it.toEntity() })
                Resource.Success(remote)
            } catch (e: Exception) {
                Resource.Error("Unable to load available currencies")
            }
        }
    }
}
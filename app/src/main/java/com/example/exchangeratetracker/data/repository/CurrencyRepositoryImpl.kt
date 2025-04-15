package com.example.exchangeratetracker.data.repository

import com.example.exchangeratetracker.BuildConfig
import com.example.exchangeratetracker.data.local.dao.CurrencyInfoDao
import com.example.exchangeratetracker.data.local.dao.CurrencyRateDao
import com.example.exchangeratetracker.data.local.mapper.toDomain
import com.example.exchangeratetracker.data.local.mapper.toEntity
import com.example.exchangeratetracker.data.local.preferences.BaseCurrencyPreferences
import com.example.exchangeratetracker.data.remote.api.OpenExchangeApi
import com.example.exchangeratetracker.data.remote.mapper.toCurrencyInfoList
import com.example.exchangeratetracker.domain.model.CurrencyInfo
import com.example.exchangeratetracker.domain.model.CurrencyRate
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: OpenExchangeApi,
    private val currencyInfoDao: CurrencyInfoDao,
    private val rateDao: CurrencyRateDao,
    private val basePrefs: BaseCurrencyPreferences
) : CurrencyRepository {

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

    override suspend fun getExchangeRatesForSymbols(symbols: List<String>): Resource<List<CurrencyRate>> {
        return try {
            val baseCode = basePrefs.baseCurrency.first()
            val baseInfo = CurrencyInfo("USD", "United States Dollar")

            val existing = rateDao.getRatesForBase(baseCode)
            val pinnedMap = existing.associate { (it.baseCode to it.targetCode) to it.isPinned }

            val response = api.getLatestRates(
                appId = BuildConfig.OPEN_EXCHANGE_API_KEY,
                symbols = symbols.joinToString(",")
            )

            val rates = response.rates.map { (targetCode, rate) ->
                val targetInfo = currencyInfoDao.getByCode(targetCode)?.toDomain()
                    ?: CurrencyInfo(targetCode, targetCode)

                val isPinned = pinnedMap[baseInfo.code to targetCode] ?: false

                CurrencyRate(baseInfo, targetInfo, rate, isPinned)
            }

            rateDao.insertRates(rates.map { it.toEntity() })

            Resource.Success(rates)

        } catch (e: Exception) {
            Resource.Error("Failed to load rates: ${e.message}")
        }
    }

    override fun observePinnedRates(): Flow<List<CurrencyRate>> {
        val base = CurrencyInfo("USD", "United States Dollar")

        return rateDao.observePinnedRates().map { entities ->
            val all = currencyInfoDao.getAll().map { it.toDomain() }
            entities.mapNotNull { entity ->
                val target = all.find { it.code == entity.targetCode }
                target?.let { entity.toDomain(base, it) }
            }
        }
    }

    override suspend fun pinCurrency(code: String) {
        rateDao.pin(code)
    }

    override suspend fun unpinCurrency(code: String) {
        rateDao.unpin(code)
    }

    override fun observeSearchRates(base: String, query: String): Flow<List<CurrencyRate>> {
        return rateDao.searchRates(base, query).map { entities ->
            val all = currencyInfoDao.getAll().map { it.toDomain() }
            entities.mapNotNull { entity ->
                val target = all.find { it.code == entity.targetCode }
                target?.let { entity.toDomain(CurrencyInfo(base, ""), it) }
            }
        }
    }
}
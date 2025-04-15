package com.example.exchangeratetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangeratetracker.data.local.entities.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRateEntity>)

    @Query("DELETE FROM currency_rates WHERE baseCode = :base")
    suspend fun deleteRatesForBase(base: String)

    @Query("SELECT * FROM currency_rates WHERE isPinned = 1")
    fun observePinnedRates(): Flow<List<CurrencyRateEntity>>

    @Query("UPDATE currency_rates SET isPinned = 1 WHERE targetCode = :code")
    suspend fun pin(code: String)

    @Query("UPDATE currency_rates SET isPinned = 0 WHERE targetCode = :code")
    suspend fun unpin(code: String)

    @Query("SELECT * FROM currency_rates WHERE baseCode = :base")
    suspend fun getRatesForBase(base: String): List<CurrencyRateEntity>

    @Query("SELECT * FROM currency_rates WHERE baseCode = :base AND (targetCode LIKE '%' || :query || '%' OR targetCode IN (SELECT code FROM currency_info WHERE name LIKE '%' || :query || '%'))")
    fun searchRates(base: String, query: String): Flow<List<CurrencyRateEntity>>
}
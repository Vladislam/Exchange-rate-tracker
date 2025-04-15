package com.example.exchangeratetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangeratetracker.data.local.entities.CurrencyRateEntity

@Dao
interface CurrencyRateDao {

    @Query("SELECT * FROM currency_rates WHERE baseCode = :base")
    suspend fun getRatesForBase(base: String): List<CurrencyRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRateEntity>)

    @Query("DELETE FROM currency_rates WHERE baseCode = :base")
    suspend fun deleteRatesForBase(base: String)
}
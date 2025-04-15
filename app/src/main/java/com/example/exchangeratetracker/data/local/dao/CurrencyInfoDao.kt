package com.example.exchangeratetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangeratetracker.data.local.entities.CurrencyInfoEntity

@Dao
interface CurrencyInfoDao {

    @Query("SELECT * FROM currency_info")
    suspend fun getAll(): List<CurrencyInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyInfoEntity>)
}
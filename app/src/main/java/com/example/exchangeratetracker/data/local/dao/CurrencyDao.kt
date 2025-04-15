package com.example.exchangeratetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangeratetracker.data.local.entities.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies WHERE isPinned = 1")
    suspend fun getPinnedCurrencies(): List<CurrencyEntity>

    @Query("SELECT * FROM currencies WHERE isPinned = 1")
    fun observePinnedCurrencies(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("DELETE FROM currencies WHERE code = :code")
    suspend fun deleteByCode(code: String)

    @Query("UPDATE currencies SET isPinned = 1 WHERE code = :code")
    suspend fun pin(code: String)

    @Query("UPDATE currencies SET isPinned = 0 WHERE code = :code")
    suspend fun unpin(code: String)
}
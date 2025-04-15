package com.example.exchangeratetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exchangeratetracker.data.local.dao.CurrencyDao
import com.example.exchangeratetracker.data.local.entities.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
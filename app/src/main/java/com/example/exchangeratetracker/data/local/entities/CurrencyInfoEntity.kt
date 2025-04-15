package com.example.exchangeratetracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_info")
data class CurrencyInfoEntity(
    @PrimaryKey val code: String,
    val name: String
)
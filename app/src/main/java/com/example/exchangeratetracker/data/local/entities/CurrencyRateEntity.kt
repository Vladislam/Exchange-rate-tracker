package com.example.exchangeratetracker.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "currency_rates",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyInfoEntity::class,
            parentColumns = ["code"],
            childColumns = ["baseCode"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CurrencyInfoEntity::class,
            parentColumns = ["code"],
            childColumns = ["targetCode"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["baseCode", "targetCode"]
)
data class CurrencyRateEntity(
    val baseCode: String,
    val targetCode: String,
    val rate: Double,
    val isPinned: Boolean = false
)
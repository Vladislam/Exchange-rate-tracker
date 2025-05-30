package com.example.exchangeratetracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey val code: String,
    val timestamp: Long = System.currentTimeMillis()
)
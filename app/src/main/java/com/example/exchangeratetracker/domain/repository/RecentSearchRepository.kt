package com.example.exchangeratetracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun observeRecentSearches(): Flow<List<String>>
    suspend fun addRecentSearch(code: String)
}
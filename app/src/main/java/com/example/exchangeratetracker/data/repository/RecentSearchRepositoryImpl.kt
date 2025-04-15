package com.example.exchangeratetracker.data.repository

import com.example.exchangeratetracker.data.local.dao.RecentSearchDao
import com.example.exchangeratetracker.data.local.entities.RecentSearchEntity
import com.example.exchangeratetracker.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchRepository {

    override fun observeRecentSearches(): Flow<List<String>> {
        return recentSearchDao.observeRecentSearches().map { recentSearch -> recentSearch.map { it.code } }
    }

    override suspend fun addRecentSearch(code: String) {
        recentSearchDao.insert(RecentSearchEntity(code))
    }
}
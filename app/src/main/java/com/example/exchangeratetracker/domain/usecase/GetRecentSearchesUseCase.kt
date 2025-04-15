package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
    private val repository: RecentSearchRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.observeRecentSearches()
}
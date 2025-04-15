package com.example.exchangeratetracker.domain.usecase

import com.example.exchangeratetracker.domain.repository.RecentSearchRepository
import javax.inject.Inject

class AddRecentSearchUseCase @Inject constructor(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(code: String) {
        repository.addRecentSearch(code)
    }
}
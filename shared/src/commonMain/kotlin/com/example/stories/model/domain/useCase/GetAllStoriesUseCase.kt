package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllStoriesUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<LoadStatus<List<History>>> {
        return historyRepository.getAllStories().map {
            LoadStatus.Data(it)
        }
    }
}

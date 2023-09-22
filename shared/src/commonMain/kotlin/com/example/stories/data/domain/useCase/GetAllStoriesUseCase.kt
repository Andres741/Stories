package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History
import com.example.stories.infrastructure.loading.LoadStatus
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

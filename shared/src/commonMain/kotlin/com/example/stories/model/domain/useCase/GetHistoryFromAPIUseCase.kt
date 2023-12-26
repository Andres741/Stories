package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.repository.HistoryRepository

class GetHistoryFromAPIUseCase(private val historyRepository: HistoryRepository) {

    suspend operator fun invoke(historyId: String, userId: String): LoadStatus<History> {
        return historyRepository.getHistory(userId = userId, historyId = historyId)
    }
}

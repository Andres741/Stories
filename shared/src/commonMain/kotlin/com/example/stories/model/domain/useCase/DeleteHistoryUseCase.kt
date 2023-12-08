package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String) {
        historyRepository.deleteHistory(historyId)
    }
}

package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class DeleteEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String) {
        historyRepository.deleteEditingHistory(historyId)
    }
}

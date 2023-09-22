package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class DeleteEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String) {
        historyRepository.deleteEditingHistory(historyId)
    }
}

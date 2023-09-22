package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class CreateEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String) {
        historyRepository.createEditingHistory(historyId)
    }
}

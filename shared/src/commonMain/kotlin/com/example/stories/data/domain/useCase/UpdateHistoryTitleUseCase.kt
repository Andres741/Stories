package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class UpdateHistoryTitleUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, newTitle: String) {
        historyRepository.updateHistoryTitle(historyId, newTitle)
    }
}

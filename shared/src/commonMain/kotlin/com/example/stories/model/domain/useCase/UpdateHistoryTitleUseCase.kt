package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class UpdateHistoryTitleUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, newTitle: String) {
        historyRepository.updateHistoryTitle(historyId, newTitle)
    }
}

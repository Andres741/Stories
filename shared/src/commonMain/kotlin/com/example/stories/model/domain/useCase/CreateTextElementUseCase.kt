package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class CreateTextElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(parentHistoryId: String, newText: String) {
        historyRepository.createTextElement(parentHistoryId, newText)
    }
}

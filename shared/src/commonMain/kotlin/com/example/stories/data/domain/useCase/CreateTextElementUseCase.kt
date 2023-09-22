package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class CreateTextElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(parentHistoryId: String, newText: String) {
        historyRepository.createTextElement(parentHistoryId, newText)
    }
}

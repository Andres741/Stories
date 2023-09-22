package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryElement

class UpdateHistoryElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(newElement: HistoryElement) {
        historyRepository.updateHistoryElement(newElement)
    }
}

package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.HistoryElement

class UpdateHistoryElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(newElement: HistoryElement) {
        historyRepository.updateHistoryElement(newElement)
    }
}

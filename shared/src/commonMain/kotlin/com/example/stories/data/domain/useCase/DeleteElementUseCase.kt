package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryElement

class DeleteElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(element: HistoryElement) {
        historyRepository.deleteEditingElement(element.id)
    }
}

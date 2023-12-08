package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.HistoryElement

class DeleteElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(element: HistoryElement) {
        historyRepository.deleteEditingElement(element.id)
    }
}

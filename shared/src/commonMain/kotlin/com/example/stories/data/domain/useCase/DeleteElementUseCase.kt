package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryElement
import kotlinx.coroutines.flow.update

class DeleteElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(element: HistoryElement) {
        CreateEditingHistoryUseCase.editingHistory.update { oldHistory ->
            if (oldHistory?.elements?.size == 1) oldHistory
            else oldHistory?.copy(elements = oldHistory.elements.filter { it.id != element.id })
        }
    }
}

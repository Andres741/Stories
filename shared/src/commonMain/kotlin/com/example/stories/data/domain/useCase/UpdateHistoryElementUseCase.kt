package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryElement
import kotlinx.coroutines.flow.update

class UpdateHistoryElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(newElement: HistoryElement) {
        CreateEditingHistoryUseCase.editingHistory.update { oldHistory ->
            oldHistory?.copy(
                elements = oldHistory.elements.map {
                    if (it.id == newElement.id) newElement
                    else it
                }
            )
        }
    }
}

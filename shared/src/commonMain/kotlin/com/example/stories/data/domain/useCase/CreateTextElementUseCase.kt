package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryMocks
import com.example.stories.data.repository.history.model.HistoryElement
import kotlinx.coroutines.flow.update

class CreateTextElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: Long, text: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(elements = it.elements + HistoryElement.Text(id = HistoryMocks.id++, text = text))
        }
    }
}

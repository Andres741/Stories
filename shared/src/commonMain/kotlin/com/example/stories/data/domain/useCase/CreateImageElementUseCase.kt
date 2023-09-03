package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryMocks
import com.example.stories.data.repository.history.model.HistoryElement
import kotlinx.coroutines.flow.update

class CreateImageElementUseCase(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(historyId: Long, imageUrl: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(elements = it.elements + HistoryElement.Image(id = HistoryMocks.id++, imageResource = imageUrl))
        }
    }
}

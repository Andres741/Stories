package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import kotlinx.coroutines.flow.update

class CreateTextElementUseCase {
    suspend operator fun invoke(historyId: Long, text: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(elements = it.elements + Element.Text(id = Mocks.id++, text = text))
        }
    }
}

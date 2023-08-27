package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.Element
import kotlinx.coroutines.flow.update

class DeleteElementUseCase {
    suspend operator fun invoke(element: Element) {
        CreateEditingHistoryUseCase.editingHistory.update { oldHistory ->
            if (oldHistory?.elements?.size == 1) oldHistory
            else oldHistory?.copy(elements = oldHistory.elements.filter { it.id != element.id })
        }
    }
}

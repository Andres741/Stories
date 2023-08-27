package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.Element
import kotlinx.coroutines.flow.update

class UpdateHistoryElementUseCase {

    suspend operator fun invoke(newElement: Element) {
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

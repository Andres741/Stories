package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import kotlinx.coroutines.flow.update

class CreateImageElementUseCase {

    suspend operator fun invoke(historyId: Long, imageUrl: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(elements = it.elements + Element.Image(id = Mocks.id++, imageResource = imageUrl))
        }
    }
}

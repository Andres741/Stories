package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.Element
import kotlinx.coroutines.flow.update

class SwapElementsUseCase {

    suspend operator fun invoke(historyId: Long, fromId: Long, toId: Long) {
        CreateEditingHistoryUseCase.editingHistory.update { oldHistory -> oldHistory ?: return@update null

            val allElements = ArrayList<Element>(oldHistory.elements.size + 1).apply {
                add(oldHistory.mainElement)
                addAll(oldHistory.elements)
            }

            val fromIndex = allElements.indexOfFirst { it.id == fromId }
            val toIndex = allElements.indexOfFirst { it.id == toId }

            if (fromIndex == -1 || toIndex == -1) return@update oldHistory

            allElements[fromIndex] = allElements[toIndex].apply { allElements[toIndex] = allElements[fromIndex] }

            oldHistory.copy(mainElement = allElements.removeAt(0), elements = allElements)
        }
    }
}

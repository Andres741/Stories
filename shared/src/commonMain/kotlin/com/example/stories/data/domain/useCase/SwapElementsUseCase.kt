package com.example.stories.data.domain.useCase

import kotlinx.coroutines.flow.update

class SwapElementsUseCase {

    suspend operator fun invoke(historyId: Long, fromId: Long, toId: Long) {
        CreateEditingHistoryUseCase.editingHistory.update { oldHistory -> oldHistory ?: return@update null

            val allElements = oldHistory.elements.toMutableList()

            val fromIndex = allElements.indexOfFirst { it.id == fromId }
            val toIndex = allElements.indexOfFirst { it.id == toId }

            if (fromIndex == -1 || toIndex == -1) return@update oldHistory

            allElements[fromIndex] = allElements[toIndex].apply { allElements[toIndex] = allElements[fromIndex] }

            oldHistory.copy(elements = allElements)
        }
    }
}

package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CreateEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {

    companion object {
        val editingHistory = MutableStateFlow(null as History?)
    }

    operator fun invoke(historyId: Long): Flow<History?> {
        return editingHistory.apply {
            value = GetAllStoriesUseCase.mockStoriesFlow.value.first { it.id == historyId }
        }
    }
}

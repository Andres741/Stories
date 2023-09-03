package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History
import kotlinx.coroutines.flow.Flow

class DeleteEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {

    operator fun invoke(): Flow<History?> {
        return CreateEditingHistoryUseCase.editingHistory.apply {
            value = null
        }
    }
}

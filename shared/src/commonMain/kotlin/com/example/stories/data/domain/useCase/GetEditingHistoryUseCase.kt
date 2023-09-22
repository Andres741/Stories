package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History
import kotlinx.coroutines.flow.Flow

class GetEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(historyId: String): Flow<History?> {
        return historyRepository.getEditingHistory(historyId)
    }
}

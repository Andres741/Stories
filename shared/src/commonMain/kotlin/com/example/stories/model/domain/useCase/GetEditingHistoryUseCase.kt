package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.History
import kotlinx.coroutines.flow.Flow

class GetEditingHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(historyId: String): Flow<History?> {
        return historyRepository.getEditingHistory(historyId)
    }
}

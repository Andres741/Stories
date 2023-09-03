package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import kotlinx.coroutines.flow.update

class UpdateHistoryTitleUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: Long, newTitle: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(
                title = newTitle
            )
        }
    }
}

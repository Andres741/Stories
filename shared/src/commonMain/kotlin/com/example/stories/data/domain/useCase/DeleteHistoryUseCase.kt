package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import kotlinx.coroutines.flow.update

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(historyId: Long) {
        GetAllStoriesUseCase.mockStoriesFlow.update {
            it.filter { history -> history.id != historyId }
        }
    }
}

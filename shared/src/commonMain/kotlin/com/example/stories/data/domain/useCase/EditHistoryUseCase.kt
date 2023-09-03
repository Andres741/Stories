package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History
import kotlinx.coroutines.flow.update

class EditHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(newHistory: History) {
        GetAllStoriesUseCase.mockStoriesFlow.update { stories ->
            stories.map {
                if (it.id == newHistory.id) newHistory
                else it
            }
        }
    }
}

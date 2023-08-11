package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.History
import kotlinx.coroutines.flow.update

class EditHistoryUseCase {
    operator fun invoke(newHistory: History) {
        GetAllStoriesUseCase.mockStoriesFlow.update { stories ->
            stories.map {
                if (it.id == newHistory.id) newHistory
                else it
            }
        }
    }
}

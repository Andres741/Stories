package com.example.stories.data.domain.useCase

import kotlinx.coroutines.flow.update

class DeleteHistoryUseCase {

    suspend operator fun invoke(historyId: Long) {
        GetAllStoriesUseCase.mockStoriesFlow.update {
            it.filter { history -> history.id != historyId }
        }
    }
}

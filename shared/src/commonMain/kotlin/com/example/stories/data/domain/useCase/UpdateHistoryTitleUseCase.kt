package com.example.stories.data.domain.useCase

import kotlinx.coroutines.flow.update

class UpdateHistoryTitleUseCase {

    suspend operator fun invoke(historyId: Long, newTitle: String) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(
                title = newTitle
            )
        }
    }
}

package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.History
import kotlinx.coroutines.flow.Flow

class DeleteEditingHistoryUseCase {

    operator fun invoke(): Flow<History?> {
        return CreateEditingHistoryUseCase.editingHistory.apply {
            value = null
        }
    }
}

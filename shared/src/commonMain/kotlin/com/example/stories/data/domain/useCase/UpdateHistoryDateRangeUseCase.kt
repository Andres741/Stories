package com.example.stories.data.domain.useCase

import com.example.stories.infrastructure.date.LocalDateRange
import kotlinx.coroutines.flow.update

class UpdateHistoryDateRangeUseCase {

    suspend operator fun invoke(historyId: Long, newDateRange: LocalDateRange) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(
                dateRange = newDateRange
            )
        }
    }
}

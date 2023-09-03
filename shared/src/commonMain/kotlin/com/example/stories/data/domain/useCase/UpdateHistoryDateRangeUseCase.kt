package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.infrastructure.date.LocalDateRange
import kotlinx.coroutines.flow.update

class UpdateHistoryDateRangeUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: Long, newDateRange: LocalDateRange) {
        CreateEditingHistoryUseCase.editingHistory.update {
            it?.copy(
                dateRange = newDateRange
            )
        }
    }
}

package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.infrastructure.date.LocalDateRange

class UpdateHistoryDateRangeUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, newDateRange: LocalDateRange) {
        historyRepository.updateHistoryDateRange(historyId, newDateRange)
    }
}

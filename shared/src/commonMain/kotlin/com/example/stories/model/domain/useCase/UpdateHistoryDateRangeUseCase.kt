package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.repository.HistoryRepository

class UpdateHistoryDateRangeUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, newDateRange: LocalDateRange) {
        historyRepository.updateHistoryDateRange(historyId, newDateRange)
    }
}

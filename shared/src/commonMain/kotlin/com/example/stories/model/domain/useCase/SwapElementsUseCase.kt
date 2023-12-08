package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class SwapElementsUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, fromId: String, toId: String) {
        historyRepository.swapElements(historyId, fromId, toId)
    }
}

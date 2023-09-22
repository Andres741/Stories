package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class SwapElementsUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String, fromId: String, toId: String) {
        historyRepository.swapElements(historyId, fromId, toId)
    }
}

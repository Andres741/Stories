package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class CreateImageElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(parentHistoryId: String, newImageResource: String) {
        historyRepository.createImageElement(parentHistoryId, newImageResource)
    }
}

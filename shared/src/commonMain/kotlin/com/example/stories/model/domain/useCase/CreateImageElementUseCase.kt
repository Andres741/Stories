package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class CreateImageElementUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(parentHistoryId: String, newImageResource: String) {
        historyRepository.createImageElement(parentHistoryId, newImageResource)
    }
}

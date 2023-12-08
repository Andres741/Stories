package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.History

class CreateBasicHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(title: String, text: String): History {
        return historyRepository.createBasicHistory(title, text)
    }
}

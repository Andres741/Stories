package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.History


class CreateBasicHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(title: String, text: String): History {
        return historyRepository.createBasicHistory(title, text)
    }
}

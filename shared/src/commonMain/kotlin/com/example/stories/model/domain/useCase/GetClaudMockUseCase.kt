package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository

class GetClaudMockUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke() = historyRepository.getClaudMock()
}

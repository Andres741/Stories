package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository

class CommitChangesUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String) = historyRepository.commitChanges(historyId)
}

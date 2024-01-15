package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.flow.first

class DeleteHistoryUseCase(
    private val historyRepository: HistoryRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(historyId: String) {
        historyRepository.deleteHistory(userId = userRepository.getLocalUser().first()?.id, historyId = historyId)
    }
}

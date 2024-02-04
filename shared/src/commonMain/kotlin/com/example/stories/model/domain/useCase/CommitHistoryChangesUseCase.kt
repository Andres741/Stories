package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.flow.first

class CommitHistoryChangesUseCase(
    private val historyRepository: HistoryRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(historyId: String): Boolean {
        val userId = userRepository.getLocalUser().first()?.id
        return historyRepository.commitChanges(userId, historyId)
    }
}

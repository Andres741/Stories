package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.delay

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository,
) {
    suspend operator fun invoke(user: User) {
        delay(3000)
        // TODO
    }
}

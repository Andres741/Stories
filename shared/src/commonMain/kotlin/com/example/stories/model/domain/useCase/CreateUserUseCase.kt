package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository,
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        profileImage: ByteArray?,
        saveLocalStories: Boolean = true,
    ): Response<User>? = withContext(NonCancellable) {
        if (name.isBlank()) return@withContext null
        userRepository.createUser(name, description, profileImage).also {
            if (saveLocalStories) {
                val stories = historyRepository.getAllStories().first()
                historyRepository.saveStoriesInClaud(
                    stories = stories,
                    userId = userRepository.getLocalUser().first()?.id ?: return@also,
                )
            }
        }
    }
}

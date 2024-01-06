package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class CreateUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(name: String, description: String, profileImage: String?): LoadStatus<User>? = withContext(NonCancellable) {
        if (name.isBlank()) return@withContext null
        userRepository.createUser(name, description, profileImage)
    }
}

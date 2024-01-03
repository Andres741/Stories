package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetLocalUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<User?> = userRepository.getLocalUser()
}

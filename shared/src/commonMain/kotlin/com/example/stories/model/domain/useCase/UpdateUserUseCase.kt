package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.UserRepository

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) = userRepository.editUser(user)
}

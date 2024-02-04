package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.UserRepository

class GetAllUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.getAllUsers()
}

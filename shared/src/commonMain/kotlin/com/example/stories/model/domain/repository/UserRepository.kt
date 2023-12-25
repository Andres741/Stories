package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User

interface UserRepository {
    suspend fun getAllUsers(): LoadStatus<List<User>>
}

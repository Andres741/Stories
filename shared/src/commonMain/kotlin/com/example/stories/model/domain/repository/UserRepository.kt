package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAllUsers(): LoadStatus<List<User>>
    suspend fun getUserById(userId: String): LoadStatus<User>
    fun getLocalUser(): Flow<User?>
    suspend fun createUser(name: String, description: String, profileImage: String?): LoadStatus<User>
}

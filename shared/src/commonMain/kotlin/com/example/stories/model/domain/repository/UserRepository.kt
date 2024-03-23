package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAllUsers(): Response<List<User>>
    suspend fun getUserById(userId: String): Response<User>
    fun getLocalUser(): Flow<User?>
    suspend fun createUser(name: String, description: String, byteArray: ByteArray?): Response<User>
    suspend fun editUser(user: User, byteArray: ByteArray?): Response<User>
}

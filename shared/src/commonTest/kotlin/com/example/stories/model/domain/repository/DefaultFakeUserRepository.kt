package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.User
import kotlinx.coroutines.flow.Flow

open class DefaultFakeUserRepository : UserRepository {
    override suspend fun getAllUsers(): Response<List<User>> = throw NotImplementedError()
    override suspend fun getUserById(userId: String): Response<User> = throw NotImplementedError()
    override fun getLocalUser(): Flow<User?> = throw NotImplementedError()
    override suspend fun createUser(
        name: String,
        description: String,
        byteArray: ByteArray?
    ): Response<User> = throw NotImplementedError()
    override suspend fun editUser(
        user: User,
        byteArray: ByteArray?
    ): Response<User> = throw NotImplementedError()
}

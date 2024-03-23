package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.toResponse
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.model.toDomain
import com.example.stories.model.domain.model.toRealm
import com.example.stories.model.domain.model.toResponse
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import com.example.stories.model.repository.dataSource.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val claudDataSource: UserClaudDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun getAllUsers(): Response<List<User>> {
        return claudDataSource.getAllUsers().map { it.toDomain() }.toResponse()
    }

    override suspend fun getUserById(userId: String): Response<User>  {
        return claudDataSource.getUserById(userId).map { it.toDomain() }.toResponse()
    }

    override fun getLocalUser(): Flow<User?> {
        return localDataSource.getLocalUser().map { it?.toDomain() }
    }

    override suspend fun createUser(
        name: String,
        description: String,
        byteArray: ByteArray?,
    ): Response<User> {
        return claudDataSource.createUser(name, description, byteArray).map {
            it.toDomain().also { newUser ->
                localDataSource.saveUser(newUser.toRealm())
            }
        }.toResponse()
    }

    override suspend fun editUser(user: User, byteArray: ByteArray?): Response<User> {
        return claudDataSource.editUser(user.toResponse(), byteArray).map { it.toDomain() }.onSuccess { editedUser ->
            localDataSource.saveUser(editedUser.toRealm())
        }.toResponse()
    }
}

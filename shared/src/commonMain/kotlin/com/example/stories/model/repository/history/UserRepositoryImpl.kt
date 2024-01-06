package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadStatus.Loading.toLoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.model.toDomain
import com.example.stories.model.domain.model.toRealm
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import com.example.stories.model.repository.dataSource.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val claudDataSource: UserClaudDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun getAllUsers(): LoadStatus<List<User>> = runCatching {
        claudDataSource.getAllUsers().map { it.toDomain() }
    }.toLoadStatus()

    override suspend fun getUserById(userId: String): LoadStatus<User> = runCatching {
        claudDataSource.getUserById(userId).toDomain()
    }.toLoadStatus()

    override fun getLocalUser(): Flow<User?> {
        return localDataSource.getLocalUser().map { it?.toDomain() }
    }

    override suspend fun createUser(
        name: String,
        description: String,
        profileImage: String?,
    ): LoadStatus<User> = runCatching {
        claudDataSource.createUser(name, description, profileImage).toDomain().also { newUser ->
            localDataSource.saveUser(newUser.toRealm())
        }
    }.toLoadStatus()
}

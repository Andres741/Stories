package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.mapToResponse
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.model.toDomain
import com.example.stories.model.domain.model.toRealm
import com.example.stories.model.domain.model.toResponse
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.model.repository.dataSource.ImageClaudDataSource
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import com.example.stories.model.repository.dataSource.UserLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val claudDataSource: UserClaudDataSource,
    private val localDataSource: UserLocalDataSource,
    private val imageClaudDataSource: ImageClaudDataSource,
) : UserRepository {
    override suspend fun getAllUsers(): Response<List<User>> {
        return claudDataSource.getAllUsers().mapToResponse { it.toDomain() }
    }

    override suspend fun getUserById(userId: String): Response<User>  {
        return claudDataSource.getUserById(userId).mapToResponse { it.toDomain() }
    }

    override fun getLocalUser(): Flow<User?> {
        return localDataSource.getLocalUser().map { it?.toDomain() }
    }

    override suspend fun createUser(
        name: String,
        description: String,
        byteArray: ByteArray?,
    ): Response<User> {
        val profileImage = byteArray?.let {
            imageClaudDataSource.sendImage(byteArray).getOrNull()?.imageName
        }

        return claudDataSource.createUser(name, description, profileImage).mapToResponse {
            it.toDomain().also { newUser ->
                localDataSource.saveUser(newUser.toRealm())
            }
        }
    }

    override suspend fun editUser(user: User, byteArray: ByteArray?): Response<User> {
        val profileImage = byteArray?.let {
            imageClaudDataSource.sendImage(byteArray).getOrNull()?.imageName
        }

        return claudDataSource.editUser(
            user = user.toResponse().copy(profileImage = profileImage),
        ).mapToResponse {
            it.toDomain().also { editedUser ->
                localDataSource.saveUser(editedUser.toRealm())
            }
        }
    }
}

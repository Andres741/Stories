package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.user.model.UserResponse

open class DefaultFakeUserClaudDataSource : UserClaudDataSource {
    override suspend fun getAllUsers(): Result<List<UserResponse>> {
        throw NotImplementedError("Method getAllUsers not implemented")
    }

    override suspend fun getUserById(userId: String): Result<UserResponse> {
        throw NotImplementedError("Method getUserById not implemented")
    }

    override suspend fun createUser(name: String, description: String, profileImage: String?): Result<UserResponse> {
        throw NotImplementedError("Method createUser not implemented")
    }

    override suspend fun editUser(user: UserResponse): Result<UserResponse> {
        throw NotImplementedError("Method editUser not implemented")
    }
}

package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.user.model.UserResponse

interface UserClaudDataSource {
    suspend fun getAllUsers(): Result<List<UserResponse>>
    suspend fun getUserById(userId: String): Result<UserResponse>
    suspend fun createUser(name: String, description: String, profileImageData: ByteArray?): Result<UserResponse>
    suspend fun editUser(user: UserResponse, profileImageData: ByteArray?): Result<UserResponse>
}

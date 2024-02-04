package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.user.model.UserResponse

interface UserClaudDataSource {
    suspend fun getAllUsers(): Result<List<UserResponse>>
    suspend fun getUserById(userId: String): Result<UserResponse>
    suspend fun createUser(name: String, description: String, profileImage: String?): Result<UserResponse>
    suspend fun editUser(user: UserResponse): Result<Unit>
}

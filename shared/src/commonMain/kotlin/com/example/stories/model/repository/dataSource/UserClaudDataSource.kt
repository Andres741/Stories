package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.user.model.UserResponse

interface UserClaudDataSource {
    suspend fun getAllUsers(): List<UserResponse>
    suspend fun getUserById(userId: String): UserResponse
    suspend fun createUser(name: String, description: String, profileImage: String?): UserResponse
    suspend fun editUser(user: UserResponse)
}

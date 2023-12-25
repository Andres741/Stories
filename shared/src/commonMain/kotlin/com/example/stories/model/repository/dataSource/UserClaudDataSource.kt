package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.user.model.UserResponse

interface UserClaudDataSource {
    suspend fun getAllUsers(): List<UserResponse>
}

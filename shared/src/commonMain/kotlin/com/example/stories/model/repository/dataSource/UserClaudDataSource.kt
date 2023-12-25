package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.history.model.UserResponse

interface UserClaudDataSource {
    suspend fun getAllUsers(): List<UserResponse>
}

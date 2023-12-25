package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadStatus.Loading.toLoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.model.toDomain
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.model.repository.dataSource.UserClaudDataSource

class UserRepositoryImpl(private val dataSource: UserClaudDataSource) : UserRepository {
    override suspend fun getAllUsers(): LoadStatus<List<User>> = runCatching {
        dataSource.getAllUsers().map { it.toDomain() }
    }.toLoadStatus()
}

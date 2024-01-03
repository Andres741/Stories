package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.local.user.model.UserRealm
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getLocalUser(): Flow<UserRealm?>
}
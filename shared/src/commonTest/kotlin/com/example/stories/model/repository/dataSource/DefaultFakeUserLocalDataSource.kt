package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.local.user.model.UserRealm
import kotlinx.coroutines.flow.Flow

open class DefaultFakeUserLocalDataSource : UserLocalDataSource {
    override fun getLocalUser(): Flow<UserRealm?> {
        throw NotImplementedError("Method getLocalUser not implemented")
    }

    override suspend fun saveUser(user: UserRealm) {
        throw NotImplementedError("Method saveUser not implemented")
    }
}

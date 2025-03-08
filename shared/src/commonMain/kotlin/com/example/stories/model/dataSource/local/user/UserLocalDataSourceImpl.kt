package com.example.stories.model.dataSource.local.user

import com.example.stories.model.dataSource.local.user.model.UserRealm
import com.example.stories.model.repository.dataSource.UserLocalDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLocalDataSourceImpl(private val realm: Realm) : UserLocalDataSource {
    override fun getLocalUser(): Flow<UserRealm?> {
        return realm.query<UserRealm>().asFlow().map { it.list.firstOrNull() }
    }

    override suspend fun saveUser(user: UserRealm) {
        realm.write {
            delete<UserRealm>()
            copyToRealm(user)
        }
    }
}

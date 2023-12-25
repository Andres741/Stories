package com.example.stories.model.dataSource.remote.history

import com.example.stories.model.dataSource.remote.history.model.UserResponse
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class UserApi(private val client: HttpClient) : UserClaudDataSource {
    companion object {
        private const val USERS_API = "http://192.168.38.55:8080/api/users/v1"
    }

    override suspend fun getAllUsers(): List<UserResponse> {
        return client.get("${USERS_API}/all").body()
    }
}

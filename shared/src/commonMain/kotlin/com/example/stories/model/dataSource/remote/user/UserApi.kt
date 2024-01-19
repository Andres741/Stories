package com.example.stories.model.dataSource.remote.user

import com.example.stories.model.dataSource.remote.setJsonBody
import com.example.stories.model.dataSource.remote.user.model.UserResponse
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put

class UserApi(private val client: HttpClient) : UserClaudDataSource {
    companion object {
        private const val USERS_API = "http://192.168.1.137:8080/api/users/v1"
    }

    override suspend fun getAllUsers(): List<UserResponse> {
        return client.get("$USERS_API/all").body()
    }

    override suspend fun getUserById(userId: String): UserResponse {
        return client.get("$USERS_API/user/$userId").body()
    }

    override suspend fun createUser(name: String, description: String, profileImage: String?): UserResponse {
        return client.post("$USERS_API/user") {
            parameter("userName", name)
            parameter("description", description)
            parameter("profileImage", profileImage)
        }.body()
    }

    override suspend fun editUser(user: UserResponse) {
        client.put("$USERS_API/edit") {
            setJsonBody(user)
        }
    }
}

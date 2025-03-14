package com.example.stories.model.dataSource.remote.user

import com.example.stories.infrastructure.loading.safeRequest
import com.example.stories.model.dataSource.remote.URLs
import com.example.stories.model.dataSource.remote.setJsonBody
import com.example.stories.model.dataSource.remote.user.model.UserResponse
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post

class UserApi(private val client: HttpClient) : UserClaudDataSource {
    companion object {
        private const val USERS_API = "${URLs.BASE_URL}/api/users/v1"
    }

    override suspend fun getAllUsers(): Result<List<UserResponse>> = safeRequest {
        client.get("$USERS_API/all").body()
    }

    override suspend fun getUserById(userId: String): Result<UserResponse> = safeRequest {
        client.get("$USERS_API/user/$userId").body()
    }

    override suspend fun createUser(
        name: String,
        description: String,
        profileImage: String?,
    ): Result<UserResponse> = safeRequest {
        client.post("$USERS_API/user") {
            parameter("userName", name)
            parameter("description", description)
            parameter("profileImage", profileImage)
        }.body()
    }

    override suspend fun editUser(user: UserResponse): Result<UserResponse> = safeRequest {
        client.post("$USERS_API/edit") {
            setJsonBody(user)
        }.body()
    }
}

package com.example.stories.model.dataSource.remote.user

import com.example.stories.infrastructure.loading.safeRequest
import com.example.stories.model.dataSource.remote.createJpegImageFormData
import com.example.stories.model.dataSource.remote.user.model.UserResponse
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserApi(private val client: HttpClient) : UserClaudDataSource {
    companion object {
        private const val USERS_API = "http://192.168.1.137:8080/api/users/v1"
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
        profileImageData: ByteArray?,
    ): Result<UserResponse> = safeRequest {
        client.submitFormWithBinaryData(
            url = "$USERS_API/user",
            formData = createJpegImageFormData(key = "profileImage", profileImageData),
        ) {
            parameter("userName", name)
            parameter("description", description)
        }.body()
    }

    override suspend fun editUser(user: UserResponse, profileImageData: ByteArray?): Result<UserResponse> = safeRequest {
        client.submitFormWithBinaryData(
            url = "$USERS_API/edit",
            formData = createJpegImageFormData(key = "profileImage", profileImageData) {
                append("user", Json.encodeToString(user))
            },
        ).body()
    }
}

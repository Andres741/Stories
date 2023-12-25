package com.example.stories.model.dataSource.remote.history.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val description: String,
    val profileImage: String?,
)

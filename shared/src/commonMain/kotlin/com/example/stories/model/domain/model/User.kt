package com.example.stories.model.domain.model

import com.example.stories.model.dataSource.local.user.model.UserRealm
import com.example.stories.model.dataSource.remote.user.model.UserResponse

data class User(
    val id: String,
    val name: String,
    val description: String,
    val profileImage: String?,
)

fun UserResponse.toDomain() = User(
    id = id,
    name = name,
    description = description,
    profileImage = profileImage,
)

fun UserRealm.toDomain() = User(
    id = _id,
    name = name,
    description = description,
    profileImage = profileImage,
)

package com.example.stories.model.domain.model

import com.example.stories.model.dataSource.local.user.model.UserRealm
import com.example.stories.model.dataSource.remote.user.model.UserResponse

data class User(
    val id: String,
    val name: String,
    val description: String,
    val profileImage: ImageUrl?,
)

fun UserResponse.toDomain() = User(
    id = id,
    name = name,
    description = description,
    profileImage = profileImage?.toImageUrl(),
)

fun List<UserResponse>.toDomain() = map { it.toDomain() }

fun UserRealm.toDomain() = User(
    id = _id,
    name = name,
    description = description,
    profileImage = profileImage?.toImageUrl(),
)

fun User.toRealm() = UserRealm().also {
    it._id = id
    it.name = name
    it.description = description
    it.profileImage = profileImage?.imageName
}

fun User.toResponse() = UserResponse(
    id = id,
    name = name,
    description = description,
    profileImage = profileImage?.imageName,
)

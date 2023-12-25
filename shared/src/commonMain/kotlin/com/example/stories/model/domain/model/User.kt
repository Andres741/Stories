package com.example.stories.model.domain.model

data class User(
    val id: String,
    val name: String,
    val description: String,
    val profileImage: String?,
    val stories: List<History>,
)

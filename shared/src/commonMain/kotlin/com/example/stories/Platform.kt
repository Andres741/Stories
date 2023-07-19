package com.example.stories

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
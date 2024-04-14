package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response

interface ImageRepository {
    suspend fun sendImage(byteArray: ByteArray): Response<String>
}

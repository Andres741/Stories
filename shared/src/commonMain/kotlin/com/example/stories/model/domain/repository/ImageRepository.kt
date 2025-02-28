package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.ImageUrl

interface ImageRepository {
    suspend fun sendImage(byteArray: ByteArray): Response<ImageUrl>
}

package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.ImageDomain

interface ImageRepository {
    suspend fun sendImage(name: String, byteArray: ByteArray): Response<Unit>
    suspend fun sendImage(imageDomain: ImageDomain): Response<Unit>
}

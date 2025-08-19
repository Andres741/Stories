package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.ImageUrl

open class DefaultFakeImageRepository : ImageRepository {
    override suspend fun sendImage(byteArray: ByteArray): Response<ImageUrl> = throw NotImplementedError()
}

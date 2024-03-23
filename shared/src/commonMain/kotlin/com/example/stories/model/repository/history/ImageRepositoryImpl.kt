package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.toResponse
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.repository.dataSource.ImageClaudDataSource

class ImageRepositoryImpl(private val imageClaudDataSource: ImageClaudDataSource) : ImageRepository {
    override suspend fun sendImage(name: String, byteArray: ByteArray): Response<Unit> {
        return imageClaudDataSource.sendImage(name, byteArray).toResponse()
    }
}

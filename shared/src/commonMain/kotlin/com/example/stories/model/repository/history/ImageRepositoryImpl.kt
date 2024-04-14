package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.toResponse
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.repository.dataSource.ImageClaudDataSource

class ImageRepositoryImpl(private val imageClaudDataSource: ImageClaudDataSource) : ImageRepository {
    override suspend fun sendImage(byteArray: ByteArray): Response<String> {
        return imageClaudDataSource.sendImage(byteArray).map { it.imageName }.toResponse()
    }
}

package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.mapToResponse
import com.example.stories.model.domain.model.ImageUrl
import com.example.stories.model.domain.model.serverImageToUrl
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.repository.dataSource.ImageClaudDataSource

class ImageRepositoryImpl(
    private val imageClaudDataSource: ImageClaudDataSource,
) : ImageRepository {
    override suspend fun sendImage(byteArray: ByteArray): Response<ImageUrl> {
        return imageClaudDataSource.sendImage(byteArray).mapToResponse {
            it.serverImageToUrl()
        }
    }
}

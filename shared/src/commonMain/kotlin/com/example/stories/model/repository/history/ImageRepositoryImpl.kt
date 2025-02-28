package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.mapToResponse
import com.example.stories.model.dataSource.remote.image.ImagesApi
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.repository.dataSource.ImageClaudDataSource

class ImageRepositoryImpl(private val imageClaudDataSource: ImageClaudDataSource) : ImageRepository {
    override suspend fun sendImage(byteArray: ByteArray): Response<String> {
        return imageClaudDataSource.sendImage(byteArray).mapToResponse {
            "${ImagesApi.IMAGES_API}/${it.imageName}"
        }
    }
}

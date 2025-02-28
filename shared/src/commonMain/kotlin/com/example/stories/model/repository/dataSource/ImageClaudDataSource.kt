package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.image.model.ImageResponse

interface ImageClaudDataSource {
    suspend fun sendImage(byteArray: ByteArray): Result<ImageResponse>
    suspend fun sendTestImage(byteArray: ByteArray): Result<ImageResponse>
}

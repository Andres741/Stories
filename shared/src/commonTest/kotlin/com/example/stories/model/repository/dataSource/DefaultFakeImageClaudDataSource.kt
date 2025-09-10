package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.image.model.ImageResponse

open class DefaultFakeImageClaudDataSource : ImageClaudDataSource {
    override suspend fun sendImage(byteArray: ByteArray): Result<ImageResponse> {
        throw NotImplementedError("Method sendImage not implemented")
    }

    override suspend fun sendTestImage(byteArray: ByteArray): Result<ImageResponse> {
        throw NotImplementedError("Method sendTestImage not implemented")
    }
}

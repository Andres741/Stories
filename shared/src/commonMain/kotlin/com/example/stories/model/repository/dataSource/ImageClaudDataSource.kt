package com.example.stories.model.repository.dataSource

interface ImageClaudDataSource {
    suspend fun sendImage(name: String, byteArray: ByteArray): Result<Unit>
}

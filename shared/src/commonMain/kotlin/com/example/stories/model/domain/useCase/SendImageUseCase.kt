package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.ImageRepository

class SendImageUseCase(private val repository: ImageRepository) {
    suspend operator fun invoke(byteArray: ByteArray) = repository.sendImage(byteArray)
}

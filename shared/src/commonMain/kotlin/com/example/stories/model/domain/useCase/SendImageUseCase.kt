package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.ImageDomain
import com.example.stories.model.domain.repository.ImageRepository

class SendImageUseCase(private val repository: ImageRepository) {
    suspend operator fun invoke(name: String, byteArray: ByteArray) = repository.sendImage(name, byteArray)
    suspend operator fun invoke(imageDomain: ImageDomain) = repository.sendImage(imageDomain)
}

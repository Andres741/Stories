package com.example.stories.model.dataSource.remote.image

import com.example.stories.infrastructure.loading.safeRequest
import com.example.stories.model.dataSource.remote.createJpegImageFormData
import com.example.stories.model.repository.dataSource.ImageClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitFormWithBinaryData

class ImagesApi(private val client: HttpClient) : ImageClaudDataSource {

    companion object {
        const val IMAGES_API = "http://192.168.1.137:8080/api/images"
    }

    override suspend fun sendImage(name: String, byteArray: ByteArray): Result<Unit> {
        return safeRequest {
            client.submitFormWithBinaryData(
                url = IMAGES_API,
                formData = createJpegImageFormData(key = "image", byteArray, name)
            )
        }
    }
}

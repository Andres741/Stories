package com.example.stories.model.dataSource.remote.image

import com.example.stories.infrastructure.loading.safeRequest
import com.example.stories.model.dataSource.remote.URLs
import com.example.stories.model.dataSource.remote.createJpegImageFormData
import com.example.stories.model.dataSource.remote.image.model.ImageResponse
import com.example.stories.model.repository.dataSource.ImageClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitFormWithBinaryData

class ImagesApi(private val client: HttpClient) : ImageClaudDataSource {
    companion object {
        const val IMAGES_API = "${URLs.BASE_URL}/api/images"
    }

    override suspend fun sendImage(byteArray: ByteArray): Result<ImageResponse> {
        return safeRequest {
            client.submitFormWithBinaryData(
                url = IMAGES_API,
                formData = createJpegImageFormData(key = "image", byteArray),
            ).body()
        }
    }
}

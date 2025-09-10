package com.example.stories.model.repository.history

import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.model.dataSource.remote.image.model.ImageResponse
import com.example.stories.model.domain.model.serverImageToUrl
import com.example.stories.model.repository.dataSource.DefaultFakeImageClaudDataSource
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImageRepositoryImplTest {

    @Test
    fun `sendImage should return ImageUrl on successful image upload`() = runBlocking {
        // Given
        val byteArrayExpected = byteArrayOf(1, 2, 3, 4, 5)
        val imageNameExpected = "test_image.jpg"
        val imageResponseExpected = ImageResponse(imageName = imageNameExpected)
        val imageUrlExpected = imageResponseExpected.serverImageToUrl() // Uses the extension function

        InvocationCounter(invocationsTarget = 1).use { sendImageClaudInvocationCounter ->
            val mockImageClaudDataSource = object : DefaultFakeImageClaudDataSource() {
                override suspend fun sendImage(byteArray: ByteArray): Result<ImageResponse> {
                    sendImageClaudInvocationCounter()
                    assertTrue(byteArrayExpected.contentEquals(byteArray), "ByteArrays should match")
                    return Result.success(imageResponseExpected)
                }
            }

            val repository = ImageRepositoryImpl(
                imageClaudDataSource = mockImageClaudDataSource
            )

            // When
            val response = repository.sendImage(byteArrayExpected)

            // Then
            response.fold(
                ifRight = { imageUrlResult ->
                    assertEquals(imageUrlExpected, imageUrlResult)
                    assertEquals(imageUrlExpected.url, imageUrlResult.url)
                },
                ifLeft = { error ->
                    throw AssertionError("Expected Right but got Left: $error")
                }
            )
        }
    }

    @Test
    fun `sendImage should return Error on failed image upload`() = runBlocking {
        // Given
        val byteArrayExpected = byteArrayOf(6, 7, 8, 9, 10)
        val exceptionExpected = Exception("Upload failed")
        val loadingErrorExpected = LoadingError.GenericError

        InvocationCounter(invocationsTarget = 1).use { sendImageClaudInvocationCounter ->
            val mockImageClaudDataSource = object : DefaultFakeImageClaudDataSource() {
                override suspend fun sendImage(byteArray: ByteArray): Result<ImageResponse> {
                    sendImageClaudInvocationCounter()
                    assertTrue(byteArrayExpected.contentEquals(byteArray))
                    return Result.failure(exceptionExpected)
                }
            }

            val repository = ImageRepositoryImpl(
                imageClaudDataSource = mockImageClaudDataSource
            )

            // When
            val response = repository.sendImage(byteArrayExpected)

            // Then
            response.fold(
                ifRight = { imageUrlResult ->
                    throw AssertionError("Expected Left but got Right: $imageUrlResult")
                },
                ifLeft = { error ->
                    assertEquals(loadingErrorExpected, error)
                    assertTrue(error is LoadingError.GenericError, "Error should be NetworkError")
                }
            )
        }
    }
}

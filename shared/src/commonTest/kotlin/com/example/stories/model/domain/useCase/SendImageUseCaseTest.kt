package com.example.stories.model.domain.useCase

import arrow.core.right
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.ImageUrl
import com.example.stories.model.domain.repository.DefaultFakeImageRepository
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SendImageUseCaseTest {

    @Test
    fun `invoke should call repository sendImage and return its result`() = runBlocking {
        // Given
        val byteArrayExpected = byteArrayOf(1, 2, 3, 4, 5)
        val imageUrlExpected = ImageUrl("http://example.com/image.jpg")
        val responseExpected: Response<ImageUrl> = imageUrlExpected.right()

        val sendImageInvocation = InvocationCounter(1)
        var capturedByteArray: ByteArray? = null

        val fakeImageRepository: ImageRepository = object : DefaultFakeImageRepository() {
            override suspend fun sendImage(byteArray: ByteArray): Response<ImageUrl> {
                sendImageInvocation()
                capturedByteArray = byteArray
                return responseExpected
            }
        }

        val useCase = SendImageUseCase(
            repository = fakeImageRepository
        )

        // When
        val result = useCase(byteArrayExpected)

        // Then
        sendImageInvocation.verifyInvocations()
        assertContentEquals(byteArrayExpected, capturedByteArray, "Captured ByteArray should match expected")
        assertEquals(responseExpected, result)
    }
}

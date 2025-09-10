package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateImageElementUseCaseTest {

    @Test
    fun `invoke should call repository createImageElement`() = runBlocking {
        // Given
        val parentHistoryIdExpected = "parent-hist-1"
        val newImageDataExpected = byteArrayOf(1, 2, 3)

        val createImageElementInvocation = InvocationCounter(invocationsTarget = 1).use { createImageElementInvocation ->


            val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
                override suspend fun createImageElement(
                    parentHistoryId: String,
                    newImageData: ByteArray
                ) {
                    createImageElementInvocation()
                    assertEquals(parentHistoryIdExpected, parentHistoryId)
                    assertTrue(newImageDataExpected.contentEquals(newImageData)) // Use contentEquals for ByteArray
                }
            }

            val useCase = CreateImageElementUseCase(historyRepository = fakeHistoryRepository)

            // When
            useCase(parentHistoryIdExpected, newImageDataExpected)
        }
    }
}
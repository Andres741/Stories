package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SwapElementsUseCaseTest {

    @Test
    fun `invoke should call repository swapElements with correct parameters`() = runBlocking {
        // Given
        val historyIdExpected = "hist-main-123"
        val fromIdExpected = "element-id-from"
        val toIdExpected = "element-id-to"

        val swapElementsInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null
        var capturedFromId: String? = null
        var capturedToId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun swapElements(historyId: String, fromId: String, toId: String) {
                swapElementsInvocation()
                capturedHistoryId = historyId
                capturedFromId = fromId
                capturedToId = toId
            }
        }

        val useCase = SwapElementsUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(historyIdExpected, fromIdExpected, toIdExpected)

        // Then
        swapElementsInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(fromIdExpected, capturedFromId)
        assertEquals(toIdExpected, capturedToId)
    }
}

package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTextElementUseCaseTest {

    @Test
    fun `invoke should call repository createTextElement`() = runBlocking {
        // Given
        val parentHistoryIdExpected = "parent-hist-2"
        val newTextExpected = "New text element content"

        val createTextElementInvocation = InvocationCounter(invocationsTarget = 1)

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun createTextElement(parentHistoryId: String, newText: String) {
                createTextElementInvocation()
                assertEquals(parentHistoryIdExpected, parentHistoryId)
                assertEquals(newTextExpected, newText)
            }
        }

        val useCase = CreateTextElementUseCase(historyRepository = fakeHistoryRepository)

        // When
        useCase(parentHistoryIdExpected, newTextExpected)

        // Then
        createTextElementInvocation.verifyInvocations()
    }
}
package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateBasicHistoryUseCaseTest {

    @Test
    fun `invoke should call repository createBasicHistory and return history`() = runBlocking {
        // Given
        val titleExpected = "Test Title"
        val textExpected = "Test content"
        val expectedHistoryExpected = History(
            id = "hist-1",
            title = titleExpected,
            dateRange = LocalDateRange.from(0, 0),
            elements = emptyList(),
        )

        InvocationCounter(invocationsTarget = 1).use { createBasicHistoryInvocation ->
            val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
                override suspend fun createBasicHistory(
                    title: String,
                    text: String
                ): History {
                    createBasicHistoryInvocation()
                    assertEquals(titleExpected, title)
                    assertEquals(textExpected, text)
                    return expectedHistoryExpected
                }
            }

            val useCase = CreateBasicHistoryUseCase(historyRepository = fakeHistoryRepository)

            // When
            val result = useCase(titleExpected, textExpected)

            // Then
            assertEquals(expectedHistoryExpected, result)
        }
    }
}
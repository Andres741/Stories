package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateEditingHistoryUseCaseTest {

    @Test
    fun `invoke should call repository createEditingHistory`() = runBlocking {
        // Given
        val historyIdExpected = "edit-hist-789"

        InvocationCounter(invocationsTarget = 1).use { createEditingHistoryInvocation ->
            val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
                override suspend fun createEditingHistory(historyId: String) {
                    createEditingHistoryInvocation()
                    assertEquals(historyIdExpected, historyId)
                }
            }

            val useCase = CreateEditingHistoryUseCase(historyRepository = fakeHistoryRepository)

            // When
            useCase(historyIdExpected)
        }
    }
}

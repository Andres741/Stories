package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteEditingHistoryUseCaseTest {

    @Test
    fun `invoke should call repository deleteEditingHistory`() = runBlocking {
        // Given
        val historyIdExpected = "history-to-delete-123"
        val deleteEditingHistoryInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun deleteEditingHistory(historyId: String) {
                deleteEditingHistoryInvocation()
                capturedHistoryId = historyId
            }
        }

        val useCase = DeleteEditingHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(historyIdExpected)

        // Then
        deleteEditingHistoryInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
    }
}

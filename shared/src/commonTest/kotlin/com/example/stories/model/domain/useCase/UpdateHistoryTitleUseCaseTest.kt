package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateHistoryTitleUseCaseTest {

    @Test
    fun `invoke should call repository updateHistoryTitle with correct parameters`() = runBlocking {
        // Given
        val historyIdExpected = "hist-title-update-123"
        val newTitleExpected = "A Brand New Title"

        val updateTitleInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null
        var capturedNewTitle: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun updateHistoryTitle(historyId: String, newTitle: String) {
                updateTitleInvocation()
                capturedHistoryId = historyId
                capturedNewTitle = newTitle
            }
        }

        val useCase = UpdateHistoryTitleUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(historyIdExpected, newTitleExpected)

        // Then
        updateTitleInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(newTitleExpected, capturedNewTitle)
    }
}

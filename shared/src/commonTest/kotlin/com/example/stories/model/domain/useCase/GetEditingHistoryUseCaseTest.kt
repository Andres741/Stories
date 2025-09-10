package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetEditingHistoryUseCaseTest {

    @Test
    fun `invoke should call repository getEditingHistory and return its flow`() = runBlocking {
        // Given
        val historyIdExpected = "editing-hist-456"
        val mockHistory = HistoryMocks().getMockStories()[0].copy(id = historyIdExpected)
        val flowExpected = flowOf(mockHistory)

        val getEditingHistoryInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getEditingHistory(historyId: String): Flow<History?> {
                getEditingHistoryInvocation()
                capturedHistoryId = historyId
                return flowExpected
            }
        }

        val useCase = GetEditingHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val resultFlow = useCase(historyIdExpected)
        val resultData = resultFlow.first() // Collect to verify content if needed

        // Then
        getEditingHistoryInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(flowExpected, resultFlow) // Check if the same flow instance is returned
        assertEquals(mockHistory, resultData)   // Optionally check the content
    }

    @Test
    fun `invoke with no history found should return flow of null`() = runBlocking {
        // Given
        val historyIdExpected = "non-existent-hist-789"
        val flowExpected = flowOf(null as History?)

        val getEditingHistoryInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getEditingHistory(historyId: String): Flow<History?> {
                getEditingHistoryInvocation()
                capturedHistoryId = historyId
                return flowExpected
            }
        }

        val useCase = GetEditingHistoryUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val resultFlow = useCase(historyIdExpected)
        val resultData = resultFlow.first()

        // Then
        getEditingHistoryInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(flowExpected, resultFlow)
        assertEquals(null, resultData)
    }
}

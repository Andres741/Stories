package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetHistoryByIdUseCaseTest {

    @Test
    fun `invoke with history found should return LoadStatus Data`() = runBlocking {
        // Given
        val historyIdExpected = "hist-found-123"
        val mockHistory = HistoryMocks().getMockStories()[0].copy(id = historyIdExpected)
        val flowFromRepository = flowOf(mockHistory)

        val getHistoryByIdInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getHistoryById(historyId: String): kotlinx.coroutines.flow.Flow<History?> {
                getHistoryByIdInvocation()
                capturedHistoryId = historyId
                return flowFromRepository
            }
        }

        val useCase = GetHistoryByIdUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val resultFlow = useCase(historyIdExpected)
        val resultStatus = resultFlow.first()

        // Then
        getHistoryByIdInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertTrue(resultStatus is LoadStatus.Data, "Result should be LoadStatus.Data")
        assertEquals(mockHistory, resultStatus.value)
    }

    @Test
    fun `invoke with history not found should return LoadStatus Error`() = runBlocking {
        // Given
        val historyIdExpected = "hist-not-found-456"
        val flowFromRepository = flowOf(null as History?)

        val getHistoryByIdInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getHistoryById(historyId: String): kotlinx.coroutines.flow.Flow<History?> {
                getHistoryByIdInvocation()
                capturedHistoryId = historyId
                return flowFromRepository
            }
        }

        val useCase = GetHistoryByIdUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val resultFlow = useCase(historyIdExpected)
        val resultStatus = resultFlow.first()

        // Then
        getHistoryByIdInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertTrue(resultStatus is LoadStatus.Error, "Result should be LoadStatus.Error")
        assertEquals(LoadingError.GenericError, resultStatus.error)
    }
}

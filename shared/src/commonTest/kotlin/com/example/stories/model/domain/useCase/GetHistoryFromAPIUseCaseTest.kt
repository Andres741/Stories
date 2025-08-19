package com.example.stories.model.domain.useCase

import arrow.core.Either
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetHistoryFromAPIUseCaseTest {

    @Test
    fun `invoke should call repository getHistory and return its response`() = runBlocking {
        // Given
        val historyIdExpected = "api-hist-789"
        val userIdExpected = "api-user-123"

        val mockHistory = HistoryMocks().getMockStories()[0].copy(id = historyIdExpected)

        val responseExpected = Either.Right(mockHistory)

        val getHistoryInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null
        var capturedUserId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getHistory(userId: String, historyId: String): Response<History> {
                getHistoryInvocation()
                capturedUserId = userId
                capturedHistoryId = historyId
                return responseExpected
            }
        }

        val useCase = GetHistoryFromAPIUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val result = useCase(historyId = historyIdExpected, userId = userIdExpected)

        // Then
        getHistoryInvocation.verifyInvocations()
        assertEquals(userIdExpected, capturedUserId)
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(responseExpected, result)
    }
}

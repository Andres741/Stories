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

class GetClaudMockUseCaseTest {

    @Test
    fun `invoke should call repository getClaudMock and return its result`() = runBlocking {
        // Given
        val mockHistory = HistoryMocks().getMockStories()[0]
        val responseExpected = Either.Right(listOf(mockHistory))

        val getClaudMockInvocation = InvocationCounter(1)

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getClaudMock(): Response<List<History>> {
                getClaudMockInvocation()
                return responseExpected
            }
        }

        val useCase = GetClaudMockUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val result = useCase()

        // Then
        getClaudMockInvocation.verifyInvocations()
        assertEquals(responseExpected, result)
    }
}

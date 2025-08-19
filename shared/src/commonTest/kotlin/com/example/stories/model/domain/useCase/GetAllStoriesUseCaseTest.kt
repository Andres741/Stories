package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
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

class GetAllStoriesUseCaseTest {

    @Test
    fun `invoke should return LoadStatus Data flow from repository`() = runBlocking {
        // Given
        val mockHistory = HistoryMocks().getMockStories()[0]
        val historiesListExpected = listOf(mockHistory)
        val flowFromRepository = flowOf(historiesListExpected)

        val getAllStoriesInvocation = InvocationCounter(1)

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getAllStories(): kotlinx.coroutines.flow.Flow<List<History>> {
                getAllStoriesInvocation()
                return flowFromRepository
            }
        }

        val useCase = GetAllStoriesUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        val resultFlow = useCase()
        val resultStatus = resultFlow.first() // Collect the first emission

        // Then
        getAllStoriesInvocation.verifyInvocations()
        assertTrue(resultStatus is LoadStatus.Data, "Result should be LoadStatus.Data")
        assertEquals(historiesListExpected, resultStatus.value)
    }
}

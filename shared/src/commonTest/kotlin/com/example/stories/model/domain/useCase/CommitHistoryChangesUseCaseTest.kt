package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.testUtil.InvocationCounter
import com.example.stories.testUtil.use
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CommitHistoryChangesUseCaseTest {

    @Test
    fun `invoke with existing user should call historyRepository commitChanges and return its result`() = runBlocking {
        // Given
        val historyIdExpected = "history-123"
        val userIdExpected = "user-abc"
        val expectedResult = true
        val user = User(id = userIdExpected, name = "Test User", description = "", profileImage = null)

        use(
            InvocationCounter(invocationsTarget = 1),
            InvocationCounter(invocationsTarget = 1)
        ) { commitChangesInvocation, getLocalUserInvocation ->
            val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
                override suspend fun commitChanges(
                    userId: String?,
                    historyId: String
                ): Boolean {
                    commitChangesInvocation()
                    assertEquals(userIdExpected, userId)
                    assertEquals(historyIdExpected, historyId)
                    return expectedResult
                }
            }

            val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
                override fun getLocalUser() = getLocalUserInvocation.count { flowOf(user) }
            }

            val useCase = CommitHistoryChangesUseCase(
                historyRepository = fakeHistoryRepository,
                userRepository = fakeUserRepository
            )

            // When
            val result = useCase(historyIdExpected)

            // Then
            commitChangesInvocation.verifyInvocations()
            getLocalUserInvocation.verifyInvocations()
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `invoke with no local user should call historyRepository commitChanges with null userId and return its result`() = runBlocking {
        // Given
        val historyIdExpected = "history-456"
        val expectedResultExpected = false

        val commitChangesInvocation = InvocationCounter(invocationsTarget = 1)
        val getLocalUserInvocation = InvocationCounter(invocationsTarget = 1)

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun commitChanges(userId: String?, historyId: String): Boolean {
                commitChangesInvocation()
                assertEquals(null, userId)
                assertEquals(historyIdExpected, historyId)
                return expectedResultExpected
            }
        }

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override fun getLocalUser() = getLocalUserInvocation.count { flowOf(null) }
        }

        val useCase = CommitHistoryChangesUseCase(
            historyRepository = fakeHistoryRepository,
            userRepository = fakeUserRepository
        )

        // When
        val result = useCase(historyIdExpected)

        // Then
        commitChangesInvocation.verifyInvocations()
        getLocalUserInvocation.verifyInvocations()
        assertEquals(expectedResultExpected, result)
    }
}
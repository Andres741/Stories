package com.example.stories.model.domain.useCase

import arrow.core.Either
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeleteHistoryUseCaseTest {

    @Test
    fun `invoke with existing user should call historyRepository deleteHistory with user's ID`() = runBlocking {
        // Given
        val historyIdExpected = "history-to-delete-123"
        val userIdExpected = "user-abc-456"
        val mockUser = User(id = userIdExpected, name = "Test User", description = "", profileImage = null)

        val getLocalUserInvocation = InvocationCounter(1)
        val deleteHistoryInvocation = InvocationCounter(1)
        var capturedUserId: String? = null
        var capturedHistoryId: String? = null

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override fun getLocalUser(): kotlinx.coroutines.flow.Flow<User?> {
                getLocalUserInvocation()
                return flowOf(mockUser)
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun deleteHistory(historyId: String, userId: String?): Response<Unit> {
                deleteHistoryInvocation()
                capturedUserId = userId
                capturedHistoryId = historyId
                return Either.Right(Unit)
            }
        }

        val useCase = DeleteHistoryUseCase(
            historyRepository = fakeHistoryRepository,
            userRepository = fakeUserRepository
        )

        // When
        useCase(historyIdExpected)

        // Then
        getLocalUserInvocation.verifyInvocations()
        deleteHistoryInvocation.verifyInvocations()
        assertEquals(userIdExpected, capturedUserId)
        assertEquals(historyIdExpected, capturedHistoryId)
    }

    @Test
    fun `invoke with no local user should call historyRepository deleteHistory with null userId`() = runBlocking {
        // Given
        val historyIdExpected = "history-to-delete-789"

        val getLocalUserInvocation = InvocationCounter(1)
        val deleteHistoryInvocation = InvocationCounter(1)
        var capturedUserId: String? = "initial_value_to_check_overwrite_to_null"
        var capturedHistoryId: String? = null

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override fun getLocalUser(): kotlinx.coroutines.flow.Flow<User?> {
                getLocalUserInvocation()
                return flowOf(null)
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun deleteHistory(historyId: String, userId: String?): Response<Unit> {
                deleteHistoryInvocation()
                capturedUserId = userId
                capturedHistoryId = historyId
                return Either.Right(Unit)
            }
        }

        val useCase = DeleteHistoryUseCase(
            historyRepository = fakeHistoryRepository,
            userRepository = fakeUserRepository
        )

        // When
        useCase(historyIdExpected)

        // Then
        getLocalUserInvocation.verifyInvocations()
        deleteHistoryInvocation.verifyInvocations()
        assertNull(capturedUserId)
        assertEquals(historyIdExpected, capturedHistoryId)
    }
}

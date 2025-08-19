package com.example.stories.model.domain.useCase

import arrow.core.left
import arrow.core.right
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetUserStoriesUseCaseTest {

    private val userIdExpected = "test-user-id"
    private val mockUser = User(id = userIdExpected, name = "Test User", description = "User desc", profileImage = null)
    val mockHistory = HistoryMocks().getMockStories()[0]

    private val mockStoriesList = listOf(mockHistory)

    @Test
    fun `invoke when user and stories succeed should return Success with Pair`() = runBlocking {
        // Given
        val getUserByIdInvocation = InvocationCounter(1)
        val getUserStoriesInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun getUserById(userId: String): Response<User> {
                getUserByIdInvocation()
                assertEquals(userIdExpected, userId)
                return mockUser.right()
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getUserStories(userId: String): Response<List<History>> {
                getUserStoriesInvocation()
                assertEquals(userIdExpected, userId)
                return mockStoriesList.right()
            }
        }

        val useCase = GetUserStoriesUseCase(fakeUserRepository, fakeHistoryRepository)

        // When
        val result = useCase(userIdExpected)

        // Then
        getUserByIdInvocation.verifyInvocations()
        getUserStoriesInvocation.verifyInvocations()
        assertTrue(result.isRight(), "Result should be Right")
        assertEquals(mockUser to mockStoriesList, result.getOrNull())
    }

    @Test
    fun `invoke when user fails and stories succeed should return user error`() = runBlocking {
        // Given
        val userErrorExpected = LoadingError.GenericError
        val getUserByIdInvocation = InvocationCounter(1)
        val getUserStoriesInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun getUserById(userId: String): Response<User> {
                getUserByIdInvocation()
                return userErrorExpected.left()
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getUserStories(userId: String): Response<List<History>> {
                getUserStoriesInvocation()
                return mockStoriesList.right()
            }
        }

        val useCase = GetUserStoriesUseCase(fakeUserRepository, fakeHistoryRepository)

        // When
        val result = useCase(userIdExpected)

        // Then
        getUserByIdInvocation.verifyInvocations()
        getUserStoriesInvocation.verifyInvocations()
        assertTrue(result.isLeft(), "Result should be Left")
        assertEquals(userErrorExpected, result.leftOrNull())
    }

    @Test
    fun `invoke when user succeeds and stories fail should return stories error`() = runBlocking {
        // Given
        val storiesErrorExpected = LoadingError.GenericError
        val getUserByIdInvocation = InvocationCounter(1)
        val getUserStoriesInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun getUserById(userId: String): Response<User> {
                getUserByIdInvocation()
                return mockUser.right()
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getUserStories(userId: String): Response<List<History>> {
                getUserStoriesInvocation()
                return storiesErrorExpected.left()
            }
        }

        val useCase = GetUserStoriesUseCase(fakeUserRepository, fakeHistoryRepository)

        // When
        val result = useCase(userIdExpected)

        // Then
        getUserByIdInvocation.verifyInvocations()
        getUserStoriesInvocation.verifyInvocations()
        assertTrue(result.isLeft(), "Result should be Left")
        assertEquals(storiesErrorExpected, result.leftOrNull())
    }

    @Test
    fun `invoke when both user and stories fail should return user error`() = runBlocking {
        // Given
        val userErrorExpected = LoadingError.GenericError
        val storiesError = LoadingError.GenericError
        val getUserByIdInvocation = InvocationCounter(1)
        val getUserStoriesInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun getUserById(userId: String): Response<User> {
                getUserByIdInvocation()
                return userErrorExpected.left()
            }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun getUserStories(userId: String): Response<List<History>> {
                getUserStoriesInvocation()
                return storiesError.left()
            }
        }

        val useCase = GetUserStoriesUseCase(fakeUserRepository, fakeHistoryRepository)

        // When
        val result = useCase(userIdExpected)

        // Then
        getUserByIdInvocation.verifyInvocations()
        getUserStoriesInvocation.verifyInvocations()
        assertTrue(result.isLeft(), "Result should be Left")
        // The use case logic is: user.leftOrNull() ?: stories.leftOrNull() ?: LoadingError.GenericError
        // So userError should be returned if present.
        assertEquals(userErrorExpected, result.leftOrNull())
    }
}

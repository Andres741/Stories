package com.example.stories.model.domain.useCase

import arrow.core.Either
import arrow.core.right
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.History
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
import kotlin.test.assertNull

class CreateUserUseCaseTest {

    private val dummyUser = User(id = "user-1", name = "Test User", description = "Desc", profileImage = null)
    private val dummyHistory = History(
        id = "hist-1",
        title = "title",
        dateRange = LocalDateRange.from(0, 0),
        elements = emptyList(),
    )

    @Test
    fun `invoke with blank name should return null and not call createUser`() = runBlocking {
        // Given
        val createUserInvocation = InvocationCounter(0)
        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun createUser(name: String, description: String, byteArray: ByteArray?): Response<User> {
                createUserInvocation()
                return dummyUser.right()
            }
        }
        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {}

        val useCase = CreateUserUseCase(userRepository = fakeUserRepository, historyRepository = fakeHistoryRepository)

        // When
        val result = useCase(name = "", description = "desc", profileImage = null)

        // Then
        assertNull(result)
        createUserInvocation.verifyInvocations()
    }

    @Test
    fun `invoke with valid name and saveLocalStories true should create user and save stories`() = runBlocking {
        // Given
        val nameExpected = "New User"
        val descriptionExpected = "User Description"
        val profileImageExpected = byteArrayOf(1, 2)
        val createdUserExpected = User(id = "new-user-id", name = nameExpected, description = descriptionExpected, profileImage = null)
        val localStoriesExpected = listOf(dummyHistory.copy(id = "local-story-1"))

        val createUserInvocation = InvocationCounter(invocationsTarget = 1)
        val getAllStoriesInvocation = InvocationCounter(invocationsTarget = 1)
        val saveStoriesInClaudInvocation = InvocationCounter(invocationsTarget = 1)
        val getLocalUserInvocation = InvocationCounter(invocationsTarget = 1) // For the saveStoriesInClaud part

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun createUser(name: String, description: String, byteArray: ByteArray?): Response<User> {
                createUserInvocation()
                assertEquals(nameExpected, name)
                assertEquals(descriptionExpected, description)
                assertEquals(profileImageExpected, byteArray)
                return createdUserExpected.right()
            }
            // This getLocalUser is called *after* createUser, for the saveStoriesInClaud part.
            // It should reflect the newly created user being available locally.
            override fun getLocalUser() = getLocalUserInvocation.count { flowOf(createdUserExpected) }
        }

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getAllStories() = getAllStoriesInvocation.count { flowOf(localStoriesExpected) }
            override suspend fun saveStoriesInClaud(stories: List<History>, userId: String): Response<Unit> {
                saveStoriesInClaudInvocation()
                assertEquals(localStoriesExpected, stories)
                assertEquals(createdUserExpected.id, userId)
                return Either.Right(Unit)
            }
        }

        val useCase = CreateUserUseCase(userRepository = fakeUserRepository, historyRepository = fakeHistoryRepository)

        // When
        val result = useCase(name = nameExpected, description = descriptionExpected, profileImage = profileImageExpected, saveLocalStories = true)

        // Then
        assertEquals(createdUserExpected.right(), result)
        createUserInvocation.verifyInvocations()
        getAllStoriesInvocation.verifyInvocations()
        saveStoriesInClaudInvocation.verifyInvocations()
        getLocalUserInvocation.verifyInvocations()
    }

    @Test
    fun `invoke with valid name and saveLocalStories false should create user but not save stories`() = runBlocking {
        // Given
        val nameExpected = "Another User"
        val descriptionExpected = "Another Desc"
        val createdUserExpected = User(id = "user-2", name = nameExpected, description = descriptionExpected, profileImage = null)

        val createUserInvocation = InvocationCounter(invocationsTarget = 1)
        val getAllStoriesInvocation = InvocationCounter(0)
        val saveStoriesInClaudInvocation = InvocationCounter(0)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun createUser(name: String, description: String, byteArray: ByteArray?): Response<User> {
                createUserInvocation()
                return createdUserExpected.right()
            }
        }
        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override fun getAllStories() = getAllStoriesInvocation.count { flowOf(emptyList<History>()) } // Should not be called
            override suspend fun saveStoriesInClaud(stories: List<History>, userId: String) = saveStoriesInClaudInvocation.count { Either.Right(Unit) } // Should not be called
        }

        val useCase = CreateUserUseCase(userRepository = fakeUserRepository, historyRepository = fakeHistoryRepository)

        // When
        val result = useCase(name = nameExpected, description = descriptionExpected, profileImage = null, saveLocalStories = false)

        // Then
        assertEquals(createdUserExpected.right(), result)
        createUserInvocation.verifyInvocations()
        getAllStoriesInvocation.verifyInvocations()
        saveStoriesInClaudInvocation.verifyInvocations()
    }

    @Test
    fun `invoke with saveLocalStories true but no local user for saving should still create user`() = runBlocking {
        // Given
        val nameExpected = "User No Save"
        val createdUserExpected = User(id = "user-no-save-id", name = nameExpected, description = "", profileImage = null)
        val localStoriesExpected = listOf(dummyHistory.copy(id = "local-story-2"))

        use(
            InvocationCounter(invocationsTarget = 1), 
            InvocationCounter(invocationsTarget = 1), 
            InvocationCounter(invocationsTarget = 0), // Should not be successfully called if user is null
            InvocationCounter(invocationsTarget = 1) // For the saveStoriesInClaud part
        ) { createUserInvocation, getAllStoriesInvocation, saveStoriesInClaudInvocation, getLocalUserInvocation ->

            val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
                override suspend fun createUser(
                    name: String,
                    description: String,
                    byteArray: ByteArray?
                ): Response<User> {
                    createUserInvocation()
                    return createdUserExpected.right()
                }

                override fun getLocalUser() =
                    getLocalUserInvocation.count { flowOf(null) } // No local user for saving stories
            }

            val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
                override fun getAllStories() =
                    getAllStoriesInvocation.count { flowOf(localStoriesExpected) }

                override suspend fun saveStoriesInClaud(
                    stories: List<History>,
                    userId: String
                ): Response<Unit> {
                    saveStoriesInClaudInvocation() // This should ideally not be reached if userId is null in use case
                    return Either.Right(Unit)
                }
            }

            val useCase = CreateUserUseCase(
                userRepository = fakeUserRepository,
                historyRepository = fakeHistoryRepository
            )

            // When
            val result =
                useCase(name = nameExpected, description = "", profileImage = null, saveLocalStories = true)

            // Then
            assertEquals(createdUserExpected.right(), result) // User creation should succeed
        }
    }
}
package com.example.stories.model.domain.useCase

import arrow.core.right
import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.successOrThrowLoadingError
import com.example.stories.model.domain.model.ImageUrl
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class GetAllUsersUseCaseTest {

    @Test
    fun `invoke should return users from repository`() = runBlocking {
        // Given
        val expectedUsers = listOf(
            User(
                id = "1",
                name = "Alice",
                description = "Description 1",
                profileImage = ImageUrl("url1")
            ),
            User(
                id = "2",
                name = "Bob",
                description = "Description 2",
                profileImage = ImageUrl("url2"),
            )
        )

        InvocationCounter(invocationsTarget = 1).use { getAllUsersInvocationCounter ->
            val fakeUserRepository = object : DefaultFakeUserRepository() {
                override suspend fun getAllUsers(): Response<List<User>> {
                    getAllUsersInvocationCounter()
                    return expectedUsers.right()
                }
            }

            val getAllUsersUseCase = GetAllUsersUseCase(fakeUserRepository)

            // When
            val actualUsers = getAllUsersUseCase().successOrThrowLoadingError()

            // Then
            assertEquals(expectedUsers, actualUsers)
        }
    }
}

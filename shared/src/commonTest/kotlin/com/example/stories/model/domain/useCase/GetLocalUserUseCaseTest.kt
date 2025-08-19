package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetLocalUserUseCaseTest {

    @Test
    fun `invoke should call repository getLocalUser and return its flow`() = runBlocking {
        // Given
        val mockUser = User(
            id = "local-user-1", 
            name = "Local User", 
            description = "A test user", 
            profileImage = null
        )
        val flowExpected = flowOf(mockUser)

        val getLocalUserInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override fun getLocalUser(): kotlinx.coroutines.flow.Flow<User?> {
                getLocalUserInvocation()
                return flowExpected
            }
        }

        val useCase = GetLocalUserUseCase(
            userRepository = fakeUserRepository
        )

        // When
        val resultFlow = useCase()
        val resultData = resultFlow.first() // Collect to verify content

        // Then
        getLocalUserInvocation.verifyInvocations()
        assertEquals(flowExpected, resultFlow) // Check if the same flow instance is returned
        assertEquals(mockUser, resultData)     // Optionally check the content
    }

    @Test
    fun `invoke with no local user should return flow of null`() = runBlocking {
        // Given
        val flowExpected = flowOf(null as User?)

        val getLocalUserInvocation = InvocationCounter(1)

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override fun getLocalUser(): kotlinx.coroutines.flow.Flow<User?> {
                getLocalUserInvocation()
                return flowExpected
            }
        }

        val useCase = GetLocalUserUseCase(
            userRepository = fakeUserRepository
        )

        // When
        val resultFlow = useCase()
        val resultData = resultFlow.first()

        // Then
        getLocalUserInvocation.verifyInvocations()
        assertEquals(flowExpected, resultFlow)
        assertEquals(null, resultData)
    }
}

package com.example.stories.model.domain.useCase

import arrow.core.right
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.DefaultFakeUserRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UpdateUserUseCaseTest {

    private val mockUserForEdit = User(id = "user-to-edit-1", name = "Original Name", description = "Original Desc", profileImage = null)
    private val dummySuccessResponse = mockUserForEdit.copy(name = "Edited Name").right() // The actual response content doesn't matter much here

    @Test
    fun `invoke with image data should call repository editUser with provided data`() = runBlocking {
        // Given
        val userExpected = mockUserForEdit
        val imageDataExpected = byteArrayOf(1, 2, 3, 4, 5)

        val editUserInvocation = InvocationCounter(1)
        var capturedUser: User? = null
        var capturedImageData: ByteArray? = null

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun editUser(user: User, byteArray: ByteArray?): Response<User> {
                editUserInvocation()
                capturedUser = user
                capturedImageData = byteArray
                return dummySuccessResponse
            }
        }

        val useCase = UpdateUserUseCase(userRepository = fakeUserRepository)

        // When
        useCase(user = userExpected, imageData = imageDataExpected)

        // Then
        editUserInvocation.verifyInvocations()
        assertEquals(userExpected, capturedUser)
        assertContentEquals(imageDataExpected, capturedImageData)
    }

    @Test
    fun `invoke without image data should call repository editUser with null image data`() = runBlocking {
        // Given
        val userExpected = mockUserForEdit

        val editUserInvocation = InvocationCounter(1)
        var capturedUser: User? = null
        var capturedImageData: ByteArray? = byteArrayOf(0) // Initialize to a non-null to check if it becomes null

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun editUser(user: User, byteArray: ByteArray?): Response<User> {
                editUserInvocation()
                capturedUser = user
                capturedImageData = byteArray
                return dummySuccessResponse
            }
        }

        val useCase = UpdateUserUseCase(userRepository = fakeUserRepository)

        // When
        useCase(user = userExpected) // imageData defaults to null

        // Then
        editUserInvocation.verifyInvocations()
        assertEquals(userExpected, capturedUser)
        assertNull(capturedImageData, "Image data should be null when not provided")
    }

    @Test
    fun `invoke with explicit null image data should call repository editUser with null image data`() = runBlocking {
        // Given
        val userExpected = mockUserForEdit

        val editUserInvocation = InvocationCounter(1)
        var capturedUser: User? = null
        var capturedImageData: ByteArray? = byteArrayOf(0) // Initialize to a non-null to check if it becomes null

        val fakeUserRepository: UserRepository = object : DefaultFakeUserRepository() {
            override suspend fun editUser(user: User, byteArray: ByteArray?): Response<User> {
                editUserInvocation()
                capturedUser = user
                capturedImageData = byteArray
                return dummySuccessResponse
            }
        }

        val useCase = UpdateUserUseCase(userRepository = fakeUserRepository)

        // When
        useCase(user = userExpected, imageData = null)

        // Then
        editUserInvocation.verifyInvocations()
        assertEquals(userExpected, capturedUser)
        assertNull(capturedImageData, "Image data should be null when explicitly passed as null")
    }
}

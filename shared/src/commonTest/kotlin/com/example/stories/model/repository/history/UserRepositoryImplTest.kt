package com.example.stories.model.repository.history

import com.example.stories.model.dataSource.local.user.model.UserRealm
import com.example.stories.model.dataSource.remote.image.model.ImageResponse
import com.example.stories.model.dataSource.remote.user.model.UserResponse
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.model.ImageUrl
import com.example.stories.model.repository.dataSource.DefaultFakeImageClaudDataSource
import com.example.stories.model.repository.dataSource.DefaultFakeUserClaudDataSource
import com.example.stories.model.repository.dataSource.DefaultFakeUserLocalDataSource
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryImplTest {

    private fun createUserRealmExpected(
        id: String = "realmUser1",
        name: String = "Realm User",
        description: String = "Realm Description",
        profileImage: String? = "realm_image.jpg"
    ): UserRealm {
        return UserRealm().apply {
            this._id = id
            this.name = name
            this.description = description
            this.profileImage = profileImage
        }
    }

    private fun createUserResponseExpected(
        id: String = "claudUser1",
        name: String = "Claud User",
        description: String = "Claud Description",
        profileImage: String? = "claud_image.jpg"
    ): UserResponse {
        return UserResponse(
            id = id,
            name = name,
            description = description,
            profileImage = profileImage
        )
    }

    private fun createDomainUserExpected(
        id: String = "domainUser1",
        name: String = "Domain User",
        description: String = "Domain Description",
        profileImageUrl: String? = "domain_image.jpg"
    ): User {
        return User(
            id = id,
            name = name,
            description = description,
            profileImage = profileImageUrl?.let { ImageUrl(it) }
        )
    }

    @Test
    fun `getAllUsers should return users from claudDataSource`() = runBlocking {
        // Given
        val userResponse1Expected = createUserResponseExpected(id = "user1", name = "User One")
        val userResponse2Expected = createUserResponseExpected(id = "user2", name = "User Two")
        val userResponsesExpected = listOf(userResponse1Expected, userResponse2Expected)

        InvocationCounter(invocationsTarget = 1).use { getAllUsersClaudInvocationCounter ->
            val mockUserClaudDataSource = object : DefaultFakeUserClaudDataSource() {
                override suspend fun getAllUsers(): Result<List<UserResponse>> {
                    getAllUsersClaudInvocationCounter()
                    return Result.success(userResponsesExpected)
                }
            }

            val repository = UserRepositoryImpl(
                claudDataSource = mockUserClaudDataSource,
                localDataSource = object : DefaultFakeUserLocalDataSource() {},
                imageClaudDataSource = object : DefaultFakeImageClaudDataSource() {}
            )

            // When
            val response = repository.getAllUsers()

            // Then
            response.fold(
                ifRight = { users ->
                    assertEquals(2, users.size)
                    assertEquals(userResponse1Expected.id, users[0].id)
                    assertEquals(userResponse1Expected.name, users[0].name)
                    assertEquals(userResponse2Expected.id, users[1].id)
                    assertEquals(userResponse2Expected.name, users[1].name)
                },
                ifLeft = { throw AssertionError("Should be Right but was Left: $it") }
            )
        }
    }

    @Test
    fun `getUserById should return user from claudDataSource`() = runBlocking {
        // Given
        val userIdExpected = "specificUser1"
        val userResponseExpected = createUserResponseExpected(id = userIdExpected, name = "Specific User")

        InvocationCounter(invocationsTarget = 1).use { getUserByIdClaudInvocationCounter ->
            val mockUserClaudDataSource = object : DefaultFakeUserClaudDataSource() {
                override suspend fun getUserById(userId: String): Result<UserResponse> {
                    getUserByIdClaudInvocationCounter()
                    assertEquals(userIdExpected, userId)
                    return Result.success(userResponseExpected)
                }
            }

            val repository = UserRepositoryImpl(
                claudDataSource = mockUserClaudDataSource,
                localDataSource = object : DefaultFakeUserLocalDataSource() {},
                imageClaudDataSource = object : DefaultFakeImageClaudDataSource() {}
            )

            // When
            val response = repository.getUserById(userIdExpected)

            // Then
            response.fold(
                ifRight = { user ->
                    assertEquals(userResponseExpected.id, user.id)
                    assertEquals(userResponseExpected.name, user.name)
                },
                ifLeft = { throw AssertionError("Should be Right but was Left: $it") }
            )
        }
    }

    @Test
    fun `getLocalUser should return user from localDataSource`() = runBlocking {
        // Given
        val userRealmExpected = createUserRealmExpected(id = "localUser", name = "Local DB User")

        InvocationCounter(invocationsTarget = 1).use { getLocalUserLocalInvocationCounter ->
            val mockUserLocalDataSource = object : DefaultFakeUserLocalDataSource() {
                override fun getLocalUser(): Flow<UserRealm?> {
                    getLocalUserLocalInvocationCounter()
                    return flowOf(userRealmExpected)
                }
            }

            val repository = UserRepositoryImpl(
                claudDataSource = object : DefaultFakeUserClaudDataSource() {},
                localDataSource = mockUserLocalDataSource,
                imageClaudDataSource = object : DefaultFakeImageClaudDataSource() {}
            )

            // When
            val resultUser = repository.getLocalUser().first()

            // Then
            assertNotNull(resultUser)
            assertEquals(userRealmExpected._id, resultUser.id)
            assertEquals(userRealmExpected.name, resultUser.name)
        }
    }

    @Test
    fun `getLocalUser should return null when localDataSource returns null`() = runBlocking {
        // Given
        InvocationCounter(invocationsTarget = 1).use { getLocalUserLocalInvocationCounter ->
            val mockUserLocalDataSource = object : DefaultFakeUserLocalDataSource() {
                override fun getLocalUser(): Flow<UserRealm?> {
                    getLocalUserLocalInvocationCounter()
                    return flowOf(null)
                }
            }

            val repository = UserRepositoryImpl(
                claudDataSource = object : DefaultFakeUserClaudDataSource() {},
                localDataSource = mockUserLocalDataSource,
                imageClaudDataSource = object : DefaultFakeImageClaudDataSource() {}
            )

            // When
            val resultUser = repository.getLocalUser().first()

            // Then
            assertNull(resultUser)
        }
    }

    @Test
    fun `createUser should call image and claud dataSources and save to local`() = runBlocking {
        // Given
        val nameExpected = "New UserName"
        val descriptionExpected = "New UserDesc"
        val byteArrayExpected = byteArrayOf(1, 2, 3)
        val imageNameExpected = "new_user_image.jpg"
        val createdUserResponseExpected = createUserResponseExpected(
            id = "newUser123",
            name = nameExpected,
            description = descriptionExpected,
            profileImage = imageNameExpected
        )

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 1), // sendImage
            InvocationCounter(invocationsTarget = 1), // createUserClaud
            InvocationCounter(invocationsTarget = 1)  // saveUserLocal
        ) { sendImageInvCounter, createUserClaudInvCounter, saveUserLocalInvCounter ->
            val mockImageClaudDataSource = object : DefaultFakeImageClaudDataSource() {
                override suspend fun sendImage(newImageData: ByteArray): Result<ImageResponse> {
                    sendImageInvCounter()
                    assertTrue(byteArrayExpected.contentEquals(newImageData))
                    return Result.success(ImageResponse(imageNameExpected))
                }
            }
            val mockUserClaudDataSource = object : DefaultFakeUserClaudDataSource() {
                override suspend fun createUser(name: String, description: String, profileImage: String?): Result<UserResponse> {
                    createUserClaudInvCounter()
                    assertEquals(nameExpected, name)
                    assertEquals(descriptionExpected, description)
                    assertEquals(imageNameExpected, profileImage)
                    return Result.success(createdUserResponseExpected)
                }
            }
            val mockUserLocalDataSource = object : DefaultFakeUserLocalDataSource() {
                override suspend fun saveUser(user: UserRealm) {
                    saveUserLocalInvCounter()
                    assertEquals(createdUserResponseExpected.id, user._id)
                    assertEquals(createdUserResponseExpected.name, user.name)
                    assertEquals(createdUserResponseExpected.profileImage, user.profileImage)
                }
            }

            val repository = UserRepositoryImpl(mockUserClaudDataSource, mockUserLocalDataSource, mockImageClaudDataSource)

            // When
            val response = repository.createUser(nameExpected, descriptionExpected, byteArrayExpected)

            // Then
            response.fold(
                ifRight = { user ->
                    assertEquals(createdUserResponseExpected.id, user.id)
                },
                ifLeft = { throw AssertionError("Should be Right but was Left: $it") }
            )
        }
    }

    @Test
    fun `createUser should not call imageDataSource if byteArray is null`() = runBlocking {
        // Given
        val nameExpected = "No Image User"
        val descriptionExpected = "Desc for no image"
        val createdUserResponseExpected = createUserResponseExpected(
            id = "noImageUser456",
            name = nameExpected,
            description = descriptionExpected,
            profileImage = null
        )

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 0), // sendImage (not called)
            InvocationCounter(invocationsTarget = 1), // createUserClaud
            InvocationCounter(invocationsTarget = 1)  // saveUserLocal
        ) { sendImageInvCounter, createUserClaudInvCounter, saveUserLocalInvCounter ->
            val mockImageClaudDataSource = object : DefaultFakeImageClaudDataSource() {
                override suspend fun sendImage(newImageData: ByteArray): Result<ImageResponse> {
                    sendImageInvCounter()
                    return Result.failure(NotImplementedError("Should not be called"))
                }
            }
            val mockUserClaudDataSource = object : DefaultFakeUserClaudDataSource() {
                override suspend fun createUser(name: String, description: String, profileImage: String?): Result<UserResponse> {
                    createUserClaudInvCounter()
                    assertEquals(nameExpected, name)
                    assertEquals(descriptionExpected, description)
                    assertNull(profileImage)
                    return Result.success(createdUserResponseExpected)
                }
            }
            val mockUserLocalDataSource = object : DefaultFakeUserLocalDataSource() {
                override suspend fun saveUser(user: UserRealm) {
                    saveUserLocalInvCounter()
                    assertEquals(createdUserResponseExpected.id, user._id)
                }
            }

            val repository = UserRepositoryImpl(mockUserClaudDataSource, mockUserLocalDataSource, mockImageClaudDataSource)

            // When
            val response = repository.createUser(nameExpected, descriptionExpected, null)

            // Then
            response.fold(
                ifRight = { user ->
                    assertEquals(createdUserResponseExpected.id, user.id)
                },
                ifLeft = { throw AssertionError("Should be Right but was Left: $it") }
            )
        }
    }

    @Test
    fun `editUser should call image and claud dataSources and save to local`() = runBlocking {
        // Given
        val userIdExpected = "editUser1"
        val originalUserDomainExpected = createDomainUserExpected(
            id = userIdExpected,
            name = "Original Name",
            description = "Original Desc",
            profileImageUrl = "original_image.jpg"
        )
        val newNameExpected = "Edited Name"
        val newDescriptionExpected = "Edited Desc"
        val newByteArrayExpected = byteArrayOf(4, 5, 6)
        val newImageNameExpected = "edited_user_image.jpg"

        val userToEditDomainExpected = originalUserDomainExpected.copy(name = newNameExpected, description = newDescriptionExpected)

        val userResponseArgumentExpected = UserResponse(
            id = userIdExpected,
            name = newNameExpected,
            description = newDescriptionExpected,
            profileImage = newImageNameExpected
        )
        val editedUserResponseExpected = userResponseArgumentExpected.copy(id = userIdExpected)

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 1), // sendImage
            InvocationCounter(invocationsTarget = 1), // editUserClaud
            InvocationCounter(invocationsTarget = 1)  // saveUserLocal
        ) { sendImageInvCounter, editUserClaudInvCounter, saveUserLocalInvCounter ->
            val mockImageClaudDataSource = object : DefaultFakeImageClaudDataSource() {
                override suspend fun sendImage(newImageData: ByteArray): Result<ImageResponse> {
                    sendImageInvCounter()
                    assertTrue(newByteArrayExpected.contentEquals(newImageData))
                    return Result.success(ImageResponse(newImageNameExpected))
                }
            }
            val mockUserClaudDataSource = object : DefaultFakeUserClaudDataSource() {
                override suspend fun editUser(user: UserResponse): Result<UserResponse> {
                    editUserClaudInvCounter()
                    assertEquals(userResponseArgumentExpected.id, user.id)
                    assertEquals(userResponseArgumentExpected.name, user.name)
                    assertEquals(userResponseArgumentExpected.description, user.description)
                    assertEquals(userResponseArgumentExpected.profileImage, user.profileImage)
                    return Result.success(editedUserResponseExpected)
                }
            }
            val mockUserLocalDataSource = object : DefaultFakeUserLocalDataSource() {
                override suspend fun saveUser(user: UserRealm) {
                    saveUserLocalInvCounter()
                    assertEquals(editedUserResponseExpected.id, user._id)
                    assertEquals(editedUserResponseExpected.name, user.name)
                    assertEquals(editedUserResponseExpected.profileImage, user.profileImage)
                }
            }

            val repository = UserRepositoryImpl(mockUserClaudDataSource, mockUserLocalDataSource, mockImageClaudDataSource)

            // When
            val response = repository.editUser(userToEditDomainExpected, newByteArrayExpected)

            // Then
            response.fold(
                ifRight = { user ->
                    assertEquals(editedUserResponseExpected.id, user.id)
                    assertEquals(newNameExpected, user.name)
                },
                ifLeft = { throw AssertionError("Should be Right but was Left: $it") }
            )
        }
    }
}

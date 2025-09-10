package com.example.stories.model.repository.history

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.toMilliseconds
import com.example.stories.model.dataSource.local.history.model.HistoryData
import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.HistoryRealm
import com.example.stories.model.dataSource.local.history.model.ImageElementRealm
import com.example.stories.model.dataSource.local.history.model.TextElementRealm
import com.example.stories.model.dataSource.remote.history.model.HistoryResponse
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.ImageResource
import com.example.stories.model.domain.repository.DefaultFakeImageRepository
import com.example.stories.model.repository.dataSource.DefaultFakeHistoryClaudDataSource
import com.example.stories.model.repository.dataSource.DefaultFakeHistoryLocalDataSource
import com.example.stories.testUtil.InvocationCounter
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.mongodb.kbson.ObjectId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HistoryRepositoryImplTest {

    // Helper to create a HistoryRealm with specific data for testing
    private fun createSampleHistoryRealm(
        id: ObjectId,
        title: String = "Test Title",
        textContent: String? = "Sample text",
        imageContent: ByteArray? = null,
        isEditing: Boolean = false,
        startDate: Long = Clock.System.now().toEpochMilliseconds(),
        endDate: Long? = null
    ): HistoryRealm {
        val elementId = ObjectId()
        val historyElementRealm = HistoryElementRealm().apply{
            this._id = elementId
            if (textContent != null) {
                this.text = TextElementRealm().apply { this.text = textContent }
            } else if (imageContent != null) {
                this.image = ImageElementRealm().apply { this.imageResource = imageContent }
            }
        }

        val historyData = HistoryData().apply {
            this.title = title
            this.startDate = startDate
            this.endDate = endDate
            if (textContent != null || imageContent != null) {
                this.elements = realmListOf(historyElementRealm)
            }
        }

        return HistoryRealm().apply {
            this._id = id
            if (isEditing) {
                this.editingData = historyData
            } else {
                this.data = historyData
            }
        }
    }

    @Test
    fun `getHistoryById should return null when local data source returns null`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId().toHexString()

        InvocationCounter(invocationsTarget = 1).use { getHistoryByIdLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override fun getHistoryById(historyId: ObjectId): Flow<HistoryRealm?> {
                    getHistoryByIdLocalInvocationCounter()
                    return flowOf(null)
                }
            }

            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {},
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            val resultHistory = repository.getHistoryById(historyIdExpected).first()

            // Then
            assertNull(resultHistory)
        }
    }

    @Test
    fun `getAllStories should return empty list when local data source returns empty list`() = runBlocking {
        // Given
        InvocationCounter(invocationsTarget = 1).use { getAllStoriesLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override fun getAllStories(): Flow<List<HistoryRealm>> {
                    getAllStoriesLocalInvocationCounter()
                    return flowOf(emptyList())
                }
            }
            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {},
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            val resultHistories = repository.getAllStories().first()

            // Then
            assertTrue(resultHistories.isEmpty())
        }
    }

    @Test
    fun `createEditingHistory should call local data source`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()

        InvocationCounter(invocationsTarget = 1).use { createEditingHistoryLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun createEditingHistory(historyId: ObjectId) {
                    createEditingHistoryLocalInvocationCounter()
                    assertEquals(historyIdExpected, historyId)
                }
            }
            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {},
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            repository.createEditingHistory(historyIdExpected.toHexString())

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `deleteHistory should call local and cloud data sources when userId is provided`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()
        val userIdExpected = "user123"
        val historyResponseExpected = HistoryResponse( // Cloud returns a simple response
            id = historyIdExpected.toHexString(),
            title = "",
            elements = emptyList(),
            startDate = 0,
            endDate = null
        )

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 1), // deleteHistoryLocal
            InvocationCounter(invocationsTarget = 1)  // deleteHistoryClaud
        ) { deleteHistoryLocalInvCounter, deleteHistoryClaudInvCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun deleteHistory(historyId: ObjectId) {
                    deleteHistoryLocalInvCounter()
                    assertEquals(historyIdExpected, historyId)
                }
            }
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                override suspend fun deleteHistory(userId: String, historyId: String): Result<HistoryResponse> {
                    deleteHistoryClaudInvCounter()
                    assertEquals(userIdExpected, userId)
                    assertEquals(historyIdExpected.toHexString(), historyId)
                    return Result.success(historyResponseExpected) // The actual value might not matter, just success
                }
            }
            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = mockHistoryClaudDataSource,
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            val response = repository.deleteHistory(historyIdExpected.toHexString(), userIdExpected)

            // Then
            assertTrue(response.isRight())
        }
    }

    @Test
    fun `deleteHistory should only call local data source when userId is null`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 1), // deleteHistoryLocal
            InvocationCounter(invocationsTarget = 0)  // deleteHistoryClaud (not called)
        ) { deleteHistoryLocalInvCounter, deleteHistoryClaudInvCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun deleteHistory(historyId: ObjectId) {
                    deleteHistoryLocalInvCounter()
                    assertEquals(historyIdExpected, historyId)
                }
            }
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                override suspend fun deleteHistory(userId: String, historyId: String): Result<HistoryResponse> {
                    deleteHistoryClaudInvCounter()
                    return Result.failure(NotImplementedError("Should not be called"))
                }
            }
            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = mockHistoryClaudDataSource,
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            val response = repository.deleteHistory(historyIdExpected.toHexString(), null)

            // Then
            assertTrue(response.isRight()) // Local deletion is considered a success
        }
    }

    @Test
    fun `deleteEditingHistory should call local data source`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()

        InvocationCounter(invocationsTarget = 1).use { deleteEditingHistoryLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun deleteEditingHistory(historyId: ObjectId) {
                    deleteEditingHistoryLocalInvocationCounter()
                    assertEquals(historyIdExpected, historyId)
                }
            }
            val repository = HistoryRepositoryImpl(
                historyLocalDataSource = mockHistoryLocalDataSource,
                historyClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {},
                imageRepository = object : DefaultFakeImageRepository() {}
            )

            // When
            repository.deleteEditingHistory(historyIdExpected.toHexString())

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `commitChanges should only call local data source when userId is null`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()

        com.example.stories.testUtil.use(
            InvocationCounter(invocationsTarget = 1), // commitChangesLocal
            InvocationCounter(invocationsTarget = 0), // getHistoryByIdForCloud
            InvocationCounter(invocationsTarget = 0)  // saveHistoryClaud
        ) { commitChangesLInvCounter, getHistoryByIdInvCounter, saveHistoryCInvCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun commitChanges(historyId: ObjectId): Boolean {
                    commitChangesLInvCounter()
                    return true
                }
                 override fun getHistoryById(historyId: ObjectId): Flow<HistoryRealm?> { // Won't be called effectively
                    getHistoryByIdInvCounter()
                    return flowOf(null)
                }
            }
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                 override suspend fun saveHistory(userId: String, history: HistoryResponse): Result<Unit> {
                    saveHistoryCInvCounter()
                    return Result.failure(NotImplementedError("Should not be called"))
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, mockHistoryClaudDataSource, object: DefaultFakeImageRepository(){})

            // When
            val result = repository.commitChanges(null, historyIdExpected.toHexString())

            // Then
            assertTrue(result)
        }
    }

    @Test
    fun `commitChanges should return false if local commit fails`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()
        val userIdExpected = "user123"

        InvocationCounter(invocationsTarget = 1).use { commitChangesInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun commitChanges(historyId: ObjectId): Boolean {
                    commitChangesInvocationCounter()
                    return false // Local commit failure
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object : DefaultFakeHistoryClaudDataSource() {}, object: DefaultFakeImageRepository(){})

            // When
            val result = repository.commitChanges(userIdExpected, historyIdExpected.toHexString())

            // Then
            assertFalse(result)
        }
    }

    @Test
    fun `createBasicHistory should call local data source and return domain history`() = runBlocking {
        // Given
        val titleExpected = "New Basic History"
        val textExpected = "Initial basic text"
        val historyIdExpected = ObjectId()
        val historyRealmReturnedExpected = createSampleHistoryRealm(historyIdExpected, titleExpected, textContent = textExpected)

        InvocationCounter(invocationsTarget = 1).use { createBasicHistoryLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun createBasicHistory(title: String, text: String): HistoryRealm {
                    createBasicHistoryLocalInvocationCounter()
                    assertEquals(titleExpected, title)
                    assertEquals(textExpected, text)
                    return historyRealmReturnedExpected
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            val resultHistory = repository.createBasicHistory(titleExpected, textExpected)

            // Then
            assertNotNull(resultHistory)
            assertEquals(historyIdExpected.toHexString(), resultHistory.id)
            assertEquals(titleExpected, resultHistory.title)
            assertTrue(resultHistory.elements.first() is HistoryElement.Text)
            assertEquals(textExpected, (resultHistory.elements.first() as HistoryElement.Text).text)
        }
    }

    @Test
    fun `createTextElement should call local data source`() = runBlocking {
        // Given
        val parentHistoryIdExpected = ObjectId()
        val newTextExpected = "New text element"

        InvocationCounter(invocationsTarget = 1).use { createTextElementLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun createTextElement(parentHistoryId: ObjectId, newText: String) {
                    createTextElementLocalInvocationCounter()
                    assertEquals(parentHistoryIdExpected, parentHistoryId)
                    assertEquals(newTextExpected, newText)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.createTextElement(parentHistoryIdExpected.toHexString(), newTextExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `createImageElement should call local data source`() = runBlocking {
        // Given
        val parentHistoryIdExpected = ObjectId()
        val newImageDataExpected = byteArrayOf(1, 2, 3)

        InvocationCounter(invocationsTarget = 1).use { createImageElementLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun createImageElement(parentHistoryId: ObjectId, newImageData: ByteArray) {
                    createImageElementLocalInvocationCounter()
                    assertEquals(parentHistoryIdExpected, parentHistoryId)
                    assertTrue(newImageDataExpected.contentEquals(newImageData))
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.createImageElement(parentHistoryIdExpected.toHexString(), newImageDataExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `deleteEditingElement should call local data source`() = runBlocking {
        // Given
        val elementIdExpected = ObjectId()

        InvocationCounter(invocationsTarget = 1).use { deleteEditingElementLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun deleteEditingElement(elementId: ObjectId) {
                    deleteEditingElementLocalInvocationCounter()
                    assertEquals(elementIdExpected, elementId)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.deleteEditingElement(elementIdExpected.toHexString())

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `updateHistoryTitle should call local data source`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()
        val newTitleExpected = "Updated Title"

        InvocationCounter(invocationsTarget = 1).use { updateHistoryTitleLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun updateHistoryTitle(historyId: ObjectId, newTitle: String) {
                    updateHistoryTitleLocalInvocationCounter()
                    assertEquals(historyIdExpected, historyId)
                    assertEquals(newTitleExpected, newTitle)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.updateHistoryTitle(historyIdExpected.toHexString(), newTitleExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `updateHistoryElement should call local data source for text element`() = runBlocking {
        // Given
        val elementIdExpected = ObjectId()
        val textExpected = "Updated text"
        val historyElementExpected = HistoryElement.Text(id = elementIdExpected.toHexString(), text = textExpected)

        InvocationCounter(invocationsTarget = 1).use { updateHistoryElementLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun updateHistoryElement(historyElement: HistoryElementRealm) {
                    updateHistoryElementLocalInvocationCounter()
                    assertEquals(elementIdExpected, historyElement._id)
                    assertNotNull(historyElement.text)
                    assertEquals(textExpected, historyElement.text?.text)
                    assertNull(historyElement.image)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.updateHistoryElement(historyElementExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `updateHistoryElement should call local data source for image element`() = runBlocking {
        // Given
        val elementIdExpected = ObjectId()
        val imageDataExpected = byteArrayOf(4,5,6)
        val imageResourceExpected = ImageResource.ByteArrayImage(imageDataExpected)
        // Assuming domain model's ImageElement holds ImageResource, and toRealm handles putting byteArray into ImageElementRealm.
        val historyElementExpected = HistoryElement.Image(id = elementIdExpected.toHexString(), imageResource = imageResourceExpected)

        InvocationCounter(invocationsTarget = 1).use { updateHistoryElementLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun updateHistoryElement(historyElement: HistoryElementRealm) {
                    updateHistoryElementLocalInvocationCounter()
                    assertEquals(elementIdExpected, historyElement._id)
                    assertNull(historyElement.text)
                    assertNotNull(historyElement.image)
                    assertTrue(imageDataExpected.contentEquals(historyElement.image?.imageResource))
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.updateHistoryElement(historyElementExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `updateHistoryDateRange should call local data source`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()
        val startDateExpected = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val endDateExpected = Clock.System.now().plus(DateTimePeriod(days = 1), TimeZone.UTC).toLocalDateTime(TimeZone.UTC).date
        val newDateRangeExpected = LocalDateRange(startDateExpected, endDateExpected)
        newDateRangeExpected.startDate
        val startDateMillisExpected = newDateRangeExpected.startDate.toMilliseconds()
        val endDateMillisExpected = newDateRangeExpected.endDate.toMilliseconds()


        InvocationCounter(invocationsTarget = 1).use { updateHistoryDateRangeLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun updateHistoryDateRange(historyId: ObjectId, startDate: Long, endDate: Long?) {
                    updateHistoryDateRangeLocalInvocationCounter()
                    assertEquals(historyIdExpected, historyId)
                    assertEquals(startDateMillisExpected, startDate)
                    assertEquals(endDateMillisExpected, endDate)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.updateHistoryDateRange(historyIdExpected.toHexString(), newDateRangeExpected)

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `swapElements should call local data source`() = runBlocking {
        // Given
        val historyIdExpected = ObjectId()
        val fromIdExpected = ObjectId()
        val toIdExpected = ObjectId()

        InvocationCounter(invocationsTarget = 1).use { swapElementsLocalInvocationCounter ->
            val mockHistoryLocalDataSource = object : DefaultFakeHistoryLocalDataSource() {
                override suspend fun swapElements(historyId: ObjectId, fromId: ObjectId, toId: ObjectId) {
                    swapElementsLocalInvocationCounter()
                    assertEquals(historyIdExpected, historyId)
                    assertEquals(fromIdExpected, fromId)
                    assertEquals(toIdExpected, toId)
                }
            }
            val repository = HistoryRepositoryImpl(mockHistoryLocalDataSource, object: DefaultFakeHistoryClaudDataSource(){}, object: DefaultFakeImageRepository(){})

            // When
            repository.swapElements(historyIdExpected.toHexString(), fromIdExpected.toHexString(), toIdExpected.toHexString())

            // Then (verification by InvocationCounter.use)
        }
    }

    @Test
    fun `getClaudMock should return stories from claud data source`() = runBlocking {
        // Given
        val historyIdExpected = "cloud1"
        val titleExpected = "Cloud Story 1"
        val historyResponseExpected = HistoryResponse(
            id = historyIdExpected,
            title = titleExpected,
            elements = emptyList(),
            startDate = 0,
            endDate = null
        )
        val historyResponsesExpected = listOf(historyResponseExpected)

        InvocationCounter(invocationsTarget = 1).use { getMockClaudInvocationCounter ->
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                override suspend fun getMock(): Result<List<HistoryResponse>> {
                    getMockClaudInvocationCounter()
                    return Result.success(historyResponsesExpected)
                }
            }
            val repository = HistoryRepositoryImpl(object: DefaultFakeHistoryLocalDataSource(){}, mockHistoryClaudDataSource, object: DefaultFakeImageRepository(){})

            // When
            val response = repository.getClaudMock()

            // Then
            response.fold(
                ifRight = { stories ->
                    assertEquals(1, stories.size)
                    assertEquals(historyIdExpected, stories[0].id)
                    assertEquals(titleExpected, stories[0].title)

                },
                ifLeft = { throw AssertionError("Should not fail") }
            )
        }
    }

    @Test
    fun `getUserStories should return stories from claud data source`() = runBlocking {
        // Given
        val userIdExpected = "userTest1"
        val historyIdExpected = "userStory1"
        val titleExpected = "User Story 1 Title"
        val historyResponseExpected = HistoryResponse(id = historyIdExpected, title = titleExpected, elements = emptyList(), startDate = 0, endDate = null)
        val historyResponsesExpected = listOf(historyResponseExpected)

        InvocationCounter(invocationsTarget = 1).use { getUserStoriesClaudInvocationCounter ->
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                override suspend fun getUserStories(userId: String): Result<List<HistoryResponse>> {
                    getUserStoriesClaudInvocationCounter()
                    assertEquals(userIdExpected, userId)
                    return Result.success(historyResponsesExpected)
                }
            }
            val repository = HistoryRepositoryImpl(object: DefaultFakeHistoryLocalDataSource(){}, mockHistoryClaudDataSource, object: DefaultFakeImageRepository(){})

            // When
            val response = repository.getUserStories(userIdExpected)

            // Then
            response.fold(
                ifRight = { stories ->
                    assertEquals(1, stories.size)
                    assertEquals(historyIdExpected, stories[0].id)
                    assertEquals(titleExpected, stories[0].title)
                },
                ifLeft = { throw AssertionError("Should not fail") }
            )
        }
    }

    @Test
    fun `getHistory should return history from claud data source`() = runBlocking {
        // Given
        val userIdExpected = "userTest1"
        val historyIdExpected = "histTest1"
        val titleExpected = "Specific History Title"
        val historyResponseExpected = HistoryResponse(id = historyIdExpected, title = titleExpected, elements = emptyList(), startDate = 0, endDate = null)

        InvocationCounter(invocationsTarget = 1).use { getHistoryClaudInvocationCounter ->
            val mockHistoryClaudDataSource = object : DefaultFakeHistoryClaudDataSource() {
                override suspend fun getHistory(userId: String, historyId: String): Result<HistoryResponse> {
                    getHistoryClaudInvocationCounter()
                    assertEquals(userIdExpected, userId)
                    assertEquals(historyIdExpected, historyId)
                    return Result.success(historyResponseExpected)
                }
            }
            val repository = HistoryRepositoryImpl(object: DefaultFakeHistoryLocalDataSource(){}, mockHistoryClaudDataSource, object: DefaultFakeImageRepository(){})

            // When
            val response = repository.getHistory(userIdExpected, historyIdExpected)

            // Then
            response.fold(
                ifRight = { story ->
                    assertEquals(historyIdExpected, story.id)
                    assertEquals(titleExpected, story.title)
                },
                ifLeft = { throw AssertionError("Should not fail") }
            )
        }
    }
}

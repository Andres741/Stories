package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.HistoryRealm
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface HistoryLocalDataSource {
    fun getHistoryById(historyId: ObjectId): Flow<HistoryRealm?>

    fun getAllStories(): Flow<List<HistoryRealm>>

    suspend fun createEditingHistory(historyId: ObjectId)

    suspend fun createBasicHistory(title: String, text: String): HistoryRealm

    suspend fun commitChanges(historyId: ObjectId): Boolean

    suspend fun deleteHistory(historyId: ObjectId)

    suspend fun deleteEditingHistory(historyId: ObjectId)

    suspend fun createTextElement(parentHistoryId: ObjectId, newText: String)

    suspend fun createImageElement(parentHistoryId: ObjectId, newImageData: ByteArray)

    suspend fun deleteEditingElement(elementId: ObjectId)

    suspend fun updateHistoryTitle(historyId: ObjectId, newTitle: String)

    suspend fun updateHistoryElement(historyElement: HistoryElementRealm)

    suspend fun updateHistoryDateRange(historyId: ObjectId, startDate: Long, endDate: Long?)

    suspend fun swapElements(historyId: ObjectId, fromId: ObjectId, toId: ObjectId)
}

package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.HistoryRealm
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

open class DefaultFakeHistoryLocalDataSource : HistoryLocalDataSource {
    override fun getHistoryById(historyId: ObjectId): Flow<HistoryRealm?> {
        throw NotImplementedError("Method not implemented")
    }

    override fun getAllStories(): Flow<List<HistoryRealm>> {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun createEditingHistory(historyId: ObjectId) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun createBasicHistory(title: String, text: String): HistoryRealm {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun commitChanges(historyId: ObjectId): Boolean {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun deleteHistory(historyId: ObjectId) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun deleteEditingHistory(historyId: ObjectId) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun createTextElement(parentHistoryId: ObjectId, newText: String) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun createImageElement(parentHistoryId: ObjectId, newImageData: ByteArray) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun deleteEditingElement(elementId: ObjectId) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun updateHistoryTitle(historyId: ObjectId, newTitle: String) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun updateHistoryElement(historyElement: HistoryElementRealm) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun updateHistoryDateRange(historyId: ObjectId, startDate: Long, endDate: Long?) {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun swapElements(historyId: ObjectId, fromId: ObjectId, toId: ObjectId) {
        throw NotImplementedError("Method not implemented")
    }
}

package com.example.stories.data.repository.history

import com.example.stories.data.localDataSource.history.HistoryLocalDataSource
import com.example.stories.data.localDataSource.history.model.dataToDomain
import com.example.stories.data.localDataSource.history.model.toDomainFlow
import com.example.stories.data.repository.history.model.History
import com.example.stories.data.repository.history.model.HistoryElement
import com.example.stories.data.repository.history.model.toRealm
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.toMilliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.mongodb.kbson.ObjectId

class HistoryRepository(
    private val historyLocalDataSource: HistoryLocalDataSource
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getHistoryById(historyId: String): Flow<History?> {
        return historyLocalDataSource.getHistoryById(ObjectId(historyId)).flatMapLatest { history ->
            history?.data?.toDomainFlow(history._id.toHexString()) ?: flowOf(null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllStories(): Flow<List<History>> {
        return historyLocalDataSource.getAllStories().flatMapLatest { stories ->
            if (stories.isEmpty()) return@flatMapLatest flowOf(emptyList())

            combine(stories.map { it.data?.toDomainFlow(it._id.toHexString())?.filterNotNull() ?: flowOf() }) { array ->
                array.asList()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getEditingHistory(historyId: String): Flow<History?> {
        return historyLocalDataSource.getHistoryById(ObjectId(historyId)).flatMapLatest { history ->
            history?.editingData?.toDomainFlow(history._id.toHexString()) ?: flowOf(null)
        }
    }

    suspend fun createEditingHistory(historyId: String) {
        historyLocalDataSource.createEditingHistory(ObjectId(historyId))
    }

    suspend fun deleteHistory(historyId: String) {
        historyLocalDataSource.deleteHistory(ObjectId(historyId))
    }

    suspend fun deleteEditingHistory(historyId: String) {
        historyLocalDataSource.deleteEditingHistory(ObjectId(historyId))
    }

    suspend fun commitChanges(historyId: String): Boolean {
        return historyLocalDataSource.commitChanges(ObjectId(historyId))
    }

    suspend fun createBasicHistory(title: String, text: String): History {
        return historyLocalDataSource.createBasicHistory(title, text).dataToDomain()!!
    }

    suspend fun createTextElement(parentHistoryId: String, newText: String) {
        historyLocalDataSource.createTextElement(ObjectId(parentHistoryId), newText)
    }

    suspend fun createImageElement(parentHistoryId: String, newImageResource: String) {
        historyLocalDataSource.createImageElement(ObjectId(parentHistoryId), newImageResource)
    }

    suspend fun deleteEditingElement(elementId: String) {
        historyLocalDataSource.deleteEditingElement(ObjectId(elementId))
    }

    suspend fun updateHistoryTitle(historyId: String, newTitle: String) {
        historyLocalDataSource.updateHistoryTitle(ObjectId(hexString = historyId), newTitle)
    }

    suspend fun updateHistoryElement(historyElement: HistoryElement) {
        historyLocalDataSource.updateHistoryElement(historyElement.toRealm())
    }

    suspend fun updateHistoryDateRange(historyId: String, newDateRange: LocalDateRange) {
        historyLocalDataSource.updateHistoryDateRange(
            historyId = ObjectId(historyId),
            startDate = newDateRange.startDate.toMilliseconds(),
            endDate = newDateRange.endDate.toMilliseconds(),
        )
    }

    suspend fun swapElements(historyId: String, fromId: String, toId: String) {
        historyLocalDataSource.swapElements(ObjectId(historyId), ObjectId(fromId), ObjectId(toId))
    }
}

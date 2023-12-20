package com.example.stories.model.repository.history

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.toMilliseconds
import com.example.stories.model.dataSource.local.history.model.dataToDomain
import com.example.stories.model.dataSource.local.history.model.toDomainFlow
import com.example.stories.model.dataSource.remote.history.model.toDomain
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.toRealm
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.mongodb.kbson.ObjectId

class HistoryRepositoryImpl(
    private val historyLocalDataSource: HistoryLocalDataSource,
    private val historyClaudDataSource: HistoryClaudDataSource,
) : HistoryRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getHistoryById(historyId: String): Flow<History?> {
        return historyLocalDataSource.getHistoryById(ObjectId(historyId)).flatMapLatest { history ->
            history?.data?.toDomainFlow(history._id.toHexString()) ?: flowOf(null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllStories(): Flow<List<History>> {
        return historyLocalDataSource.getAllStories().flatMapLatest { stories ->
            if (stories.isEmpty()) return@flatMapLatest flowOf(emptyList())

            combine(stories.map { it.data?.toDomainFlow(it._id.toHexString())?.filterNotNull() ?: flowOf() }) { array ->
                array.asList()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEditingHistory(historyId: String): Flow<History?> {
        return historyLocalDataSource.getHistoryById(ObjectId(historyId)).flatMapLatest { history ->
            history?.editingData?.toDomainFlow(history._id.toHexString()) ?: flowOf(null)
        }
    }

    override suspend fun createEditingHistory(historyId: String) {
        historyLocalDataSource.createEditingHistory(ObjectId(historyId))
    }

    override suspend fun deleteHistory(historyId: String) {
        historyLocalDataSource.deleteHistory(ObjectId(historyId))
    }

    override suspend fun deleteEditingHistory(historyId: String) {
        historyLocalDataSource.deleteEditingHistory(ObjectId(historyId))
    }

    override suspend fun commitChanges(historyId: String): Boolean {
        return historyLocalDataSource.commitChanges(ObjectId(historyId))
    }

    override suspend fun createBasicHistory(title: String, text: String): History {
        return historyLocalDataSource.createBasicHistory(title, text).dataToDomain()!!
    }

    override suspend fun createTextElement(parentHistoryId: String, newText: String) {
        historyLocalDataSource.createTextElement(ObjectId(parentHistoryId), newText)
    }

    override suspend fun createImageElement(parentHistoryId: String, newImageResource: String) {
        historyLocalDataSource.createImageElement(ObjectId(parentHistoryId), newImageResource)
    }

    override suspend fun deleteEditingElement(elementId: String) {
        historyLocalDataSource.deleteEditingElement(ObjectId(elementId))
    }

    override suspend fun updateHistoryTitle(historyId: String, newTitle: String) {
        historyLocalDataSource.updateHistoryTitle(ObjectId(hexString = historyId), newTitle)
    }

    override suspend fun updateHistoryElement(historyElement: HistoryElement) {
        historyLocalDataSource.updateHistoryElement(historyElement.toRealm())
    }

    override suspend fun updateHistoryDateRange(historyId: String, newDateRange: LocalDateRange) {
        historyLocalDataSource.updateHistoryDateRange(
            historyId = ObjectId(historyId),
            startDate = newDateRange.startDate.toMilliseconds(),
            endDate = newDateRange.endDate.toMilliseconds(),
        )
    }

    override suspend fun swapElements(historyId: String, fromId: String, toId: String) {
        historyLocalDataSource.swapElements(ObjectId(historyId), ObjectId(fromId), ObjectId(toId))
    }

    override suspend fun getClaudMock() = historyClaudDataSource.getMock().map { it.toDomain() }
}

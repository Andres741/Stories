package com.example.stories.model.repository.history

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.toMilliseconds
import com.example.stories.infrastructure.loading.Response
import com.example.stories.infrastructure.loading.toResponse
import com.example.stories.model.dataSource.local.history.model.dataToDomain
import com.example.stories.model.dataSource.local.history.model.toDomainFlow
import com.example.stories.model.dataSource.remote.history.model.toDomain
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.toRealm
import com.example.stories.model.domain.model.toResponse
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.mongodb.kbson.ObjectId

class HistoryRepositoryImpl(
    private val historyLocalDataSource: HistoryLocalDataSource,
    private val historyClaudDataSource: HistoryClaudDataSource,
    private val imageRepository: ImageRepository,
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

    override suspend fun deleteHistory(historyId: String, userId: String?): Response<Unit> {
        return coroutineScope {
            val response = if (userId != null) async {
                historyClaudDataSource.deleteHistory(userId, historyId).map { Unit }
            } else null
            historyLocalDataSource.deleteHistory(ObjectId(historyId))

            response?.await() ?: Result.success(Unit)
        }.toResponse()
    }

    override suspend fun deleteEditingHistory(historyId: String) {
        historyLocalDataSource.deleteEditingHistory(ObjectId(historyId))
    }

    override suspend fun commitChanges(userId: String?, historyId: String): Boolean {
        return historyLocalDataSource.commitChanges(ObjectId(historyId)).also { userSavedInLocal ->
            if (userSavedInLocal && userId != null) {
                val history = getHistoryById(historyId).first() ?: return@also

                return runCatching {
                    val idToImageName = coroutineScope {
                        history.elements
                            .asSequence()
                            .mapNotNull { it.id to (it.getImageData() ?: return@mapNotNull null) }
                            .map { (id, data) ->
                                async {
                                    id to imageRepository.sendImage(data).getOrNull()
                                }
                            }
                            .toList()
                            .awaitAll()
                            .toMap()
                    }
                    historyClaudDataSource.saveHistory(userId, history.toResponse(idToImageName))
                }.fold(
                    onSuccess = { true },
                    onFailure = { false },
                )
            }
        }
    }

    override suspend fun createBasicHistory(title: String, text: String): History {
        return historyLocalDataSource.createBasicHistory(title, text).dataToDomain()!!
    }

    override suspend fun createTextElement(parentHistoryId: String, newText: String) {
        historyLocalDataSource.createTextElement(ObjectId(parentHistoryId), newText)
    }

    override suspend fun createImageElement(parentHistoryId: String, newImageData: ByteArray) {
        historyLocalDataSource.createImageElement(ObjectId(parentHistoryId), newImageData)
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

    override suspend fun getClaudMock(): Response<List<History>> = historyClaudDataSource.getMock().map {
        it.toDomain()
    }.toResponse()

    override suspend fun getUserStories(userId: String): Response<List<History>> {
        return historyClaudDataSource.getUserStories(userId).map { it.toDomain() }.toResponse()
    }

    override suspend fun getHistory(userId: String, historyId: String): Response<History> {
        return historyClaudDataSource.getHistory(userId = userId, historyId = historyId).map {
            it.toDomain()
        }.toResponse()
    }

    override suspend fun saveStoriesInClaud(stories: List<History>, userId: String): Response<Unit> {
        return coroutineScope {
            stories.map { history ->
                async {
                    historyClaudDataSource.saveHistory(userId, history.toResponse())
                }
            }.awaitAll().forEach { item ->
                item.onFailure { return@coroutineScope item }
            }
            runCatching {  }
        }.toResponse()
    }
}

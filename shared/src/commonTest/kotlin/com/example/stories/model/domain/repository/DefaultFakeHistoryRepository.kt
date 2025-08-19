package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.loading.Response
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import kotlinx.coroutines.flow.Flow

open class DefaultFakeHistoryRepository : HistoryRepository {
    override fun getHistoryById(historyId: String): Flow<History?> = throw NotImplementedError()
    override fun getAllStories(): Flow<List<History>> = throw NotImplementedError()
    override fun getEditingHistory(historyId: String): Flow<History?> = throw NotImplementedError()
    override suspend fun createEditingHistory(historyId: String): Unit = throw NotImplementedError()
    override suspend fun deleteHistory(historyId: String, userId: String?): Response<Unit> = throw NotImplementedError()
    override suspend fun deleteEditingHistory(historyId: String) = throw NotImplementedError()
    override suspend fun commitChanges(userId: String?, historyId: String): Boolean = throw NotImplementedError()
    override suspend fun createBasicHistory(title: String, text: String): History = throw NotImplementedError()
    override suspend fun createTextElement(parentHistoryId: String, newText: String): Unit = throw NotImplementedError()
    override suspend fun createImageElement(parentHistoryId: String, newImageData: ByteArray): Unit = throw NotImplementedError()
    override suspend fun deleteEditingElement(elementId: String) = throw NotImplementedError()
    override suspend fun updateHistoryTitle(historyId: String, newTitle: String) = throw NotImplementedError()
    override suspend fun updateHistoryElement(historyElement: HistoryElement) = throw NotImplementedError()
    override suspend fun updateHistoryDateRange(historyId: String, newDateRange: LocalDateRange) = throw NotImplementedError()
    override suspend fun swapElements(historyId: String, fromId: String, toId: String) = throw NotImplementedError()
    override suspend fun getClaudMock(): Response<List<History>> = throw NotImplementedError()
    override suspend fun getUserStories(userId: String): Response<List<History>> = throw NotImplementedError()
    override suspend fun getHistory(userId: String, historyId: String): Response<History> = throw NotImplementedError()
    override suspend fun saveStoriesInClaud(stories: List<History>, userId: String): Response<Unit> = throw NotImplementedError()
}
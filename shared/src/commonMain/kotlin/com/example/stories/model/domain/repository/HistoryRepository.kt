package com.example.stories.model.domain.repository

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryById(historyId: String): Flow<History?>
    fun getAllStories(): Flow<List<History>>
    fun getEditingHistory(historyId: String): Flow<History?>
    suspend fun createEditingHistory(historyId: String)
    suspend fun deleteHistory(historyId: String)
    suspend fun deleteEditingHistory(historyId: String)
    suspend fun commitChanges(historyId: String): Boolean
    suspend fun createBasicHistory(title: String, text: String): History
    suspend fun createTextElement(parentHistoryId: String, newText: String)
    suspend fun createImageElement(parentHistoryId: String, newImageResource: String)
    suspend fun deleteEditingElement(elementId: String)
    suspend fun updateHistoryTitle(historyId: String, newTitle: String)
    suspend fun updateHistoryElement(historyElement: HistoryElement)
    suspend fun updateHistoryDateRange(historyId: String, newDateRange: LocalDateRange)
    suspend fun swapElements(historyId: String, fromId: String, toId: String)
    suspend fun getClaudMock(): List<History>
}

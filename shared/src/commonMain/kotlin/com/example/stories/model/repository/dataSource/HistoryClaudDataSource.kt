package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse

interface HistoryClaudDataSource {
    suspend fun getMock(): List<HistoryResponse>
    suspend fun getUserStories(userId: String): List<HistoryResponse>
    suspend fun getHistory(userId: String, historyId: String): HistoryResponse
    suspend fun saveHistory(userId: String, history: HistoryResponse)
}

package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse

interface HistoryClaudDataSource {
    suspend fun getMock(): Result<List<HistoryResponse>>
    suspend fun getUserStories(userId: String): Result<List<HistoryResponse>>
    suspend fun getHistory(userId: String, historyId: String): Result<HistoryResponse>
    suspend fun saveHistory(userId: String, history: HistoryResponse): Result<Unit>
    suspend fun deleteHistory(userId: String, historyId: String): Result<HistoryResponse>
}

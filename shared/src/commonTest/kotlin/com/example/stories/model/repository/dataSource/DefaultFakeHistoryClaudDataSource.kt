package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse

open class DefaultFakeHistoryClaudDataSource : HistoryClaudDataSource {
    override suspend fun getMock(): Result<List<HistoryResponse>> {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun getUserStories(userId: String): Result<List<HistoryResponse>> {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun getHistory(userId: String, historyId: String): Result<HistoryResponse> {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun saveHistory(userId: String, history: HistoryResponse): Result<Unit> {
        throw NotImplementedError("Method not implemented")
    }

    override suspend fun deleteHistory(userId: String, historyId: String): Result<HistoryResponse> {
        throw NotImplementedError("Method not implemented")
    }
}

package com.example.stories.model.repository.dataSource

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse

interface HistoryClaudDataSource {
    suspend fun getMock(): List<HistoryResponse>
}

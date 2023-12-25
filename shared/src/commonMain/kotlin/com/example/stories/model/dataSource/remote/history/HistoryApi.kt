package com.example.stories.model.dataSource.remote.history

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class HistoryApi(private val client: HttpClient) : HistoryClaudDataSource {

    companion object {
        private const val HISTORY_API = "http://192.168.38.55:8080/api/history/v1"
    }

    override suspend fun getMock(): List<HistoryResponse> {
        return client.get("$HISTORY_API/mock").body()
    }
}

package com.example.stories.model.dataSource.remote.history

import com.example.stories.model.dataSource.remote.history.model.HistoryResponse
import com.example.stories.model.dataSource.remote.setJsonBody
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put

class HistoryApi(private val client: HttpClient) : HistoryClaudDataSource {

    companion object {
        private const val HISTORY_API = "http://192.168.1.137:8080/api/history/v1"
    }

    override suspend fun getMock(): List<HistoryResponse> {
        return client.get("$HISTORY_API/mock").body()
    }

    override suspend fun getUserStories(userId: String): List<HistoryResponse>  {
        return client.get("$HISTORY_API/user?userId=$userId").body()
    }

    override suspend fun getHistory(userId: String, historyId: String): HistoryResponse  {
        return client.get("$HISTORY_API/history") {
            parameter("userId", userId)
            parameter("historyId", historyId)
        }.body()
    }

    override suspend fun saveHistory(userId: String, history: HistoryResponse) {
        client.put("$HISTORY_API/history") {
            parameter("userId", userId)
            setJsonBody(history)
        }
    }
}

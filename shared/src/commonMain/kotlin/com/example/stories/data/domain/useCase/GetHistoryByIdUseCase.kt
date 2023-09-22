package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.model.History
import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHistoryByIdUseCase(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(id: String): Flow<LoadStatus<History>> {
        return historyRepository.getHistoryById(id).map {
            it?.let { LoadStatus.Data(it) } ?: LoadStatus.Error(LoadingError.GenericError)
        }
    }
}

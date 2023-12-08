package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.model.History
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

package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetHistoryByIdUseCase {

    private fun getData(id: Long): Flow<History?> = GetAllStoriesUseCase.mockStoriesFlow.map { stories ->
        stories.find { history -> history.id == id }
    }

    operator fun invoke(id: Long): Flow<LoadStatus<History>> = flow {
        delay(1000)
        emit(LoadStatus.Error(LoadingError.GenericError))
        delay(1000)
        getData(id).collect { history ->
            emit(LoadStatus.Loading)
            delay(1000)

            history?.let {
                emit(LoadStatus.Data(history))
            } ?: emit(LoadStatus.Error(LoadingError.GenericError))
        }
    }
}

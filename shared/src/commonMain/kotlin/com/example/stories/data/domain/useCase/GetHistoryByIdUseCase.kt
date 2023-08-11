package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.GetAllStoriesUseCase.Companion.delayTime
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
        delay(delayTime)
        emit(LoadStatus.Error(LoadingError.GenericError))
        delay(delayTime)
        emit(LoadStatus.Loading)
        delay(delayTime)
        getData(id).collect { history ->
            history?.let {
                emit(LoadStatus.Data(history))
            } ?: emit(LoadStatus.Error(LoadingError.GenericError))
        }
    }
}

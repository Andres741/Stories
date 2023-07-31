package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class GetAllStoriesUseCase {

    companion object {
        val mockStoriesFlow = MutableStateFlow(Mocks().getMockStories())
    }

    operator fun invoke(): Flow<LoadStatus<List<History>>> = flow {
        delay(1000)
        emit(LoadStatus.Error(LoadingError.GenericError))
        delay(1000)
        mockStoriesFlow.collect {
            emit(LoadStatus.Loading)
            delay(1000)
            emit(LoadStatus.Data(it))
        }
    }
}

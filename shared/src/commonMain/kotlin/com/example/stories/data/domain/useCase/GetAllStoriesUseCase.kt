package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryMocks
import com.example.stories.data.repository.history.model.History
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.LoadingError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class GetAllStoriesUseCase(
    private val historyRepository: HistoryRepository
) {
    companion object {
        const val delayTime = 100L
        val mockStoriesFlow = MutableStateFlow(HistoryMocks().getMockStories())
    }

    operator fun invoke(): Flow<LoadStatus<List<History>>> = flow {
        delay(delayTime)
        emit(LoadStatus.Error(LoadingError.GenericError))
        delay(delayTime)
        mockStoriesFlow.collect {
            emit(LoadStatus.Loading)
            delay(delayTime)
            emit(LoadStatus.Data(it))
        }
    }
}

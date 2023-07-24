package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHistoryByIdUseCase {

    operator fun invoke(id: Long): Flow<History?> = GetAllStoriesUseCase.mockStoriesFlow.map { stories ->
        stories.find { history -> history.id == id }
    }
}
package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.History
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetAllStoriesUseCase {

    companion object {
        val mockStoriesFlow = MutableStateFlow(Mocks().getMockStories())
    }

    operator fun invoke(): Flow<List<History>> = mockStoriesFlow

}

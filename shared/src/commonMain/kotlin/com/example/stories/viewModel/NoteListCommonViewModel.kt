package com.example.stories.viewModel

import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.DeleteHistoryUseCase
import com.example.stories.data.domain.useCase.GetAllStoriesUseCase
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteListCommonViewModel(
    coroutineScope: CoroutineScope? = null
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val getAllStoriesUseCase: GetAllStoriesUseCase = GetAllStoriesUseCase()
    private val deleteHistoryUseCase = DeleteHistoryUseCase()

    val storiesLoadStatus: CommonStateFlow<LoadStatus<List<History>>> = getAllStoriesUseCase.invoke().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LoadStatus.Loading
    ).toCommonStateFlow()

    fun deleteHistory(historyId: Long) {
        viewModelScope.launch {
            deleteHistoryUseCase(historyId)
        }
    }

    fun dispose() {
        viewModelScope.cancel()
    }
}

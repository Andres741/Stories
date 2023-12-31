package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.data.domain.useCase.DeleteHistoryUseCase
import com.example.stories.data.domain.useCase.GetAllStoriesUseCase
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.get

class StoriesListCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getAllStoriesUseCase: GetAllStoriesUseCase = Component.get(),
    private val deleteHistoryUseCase: DeleteHistoryUseCase = Component.get(),
    private val createBasicHistoryUseCase: CreateBasicHistoryUseCase = Component.get(),
) {

    constructor(): this(coroutineScope = null)

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    val storiesLoadStatus: CommonStateFlow<LoadStatus<List<History>>> = getAllStoriesUseCase.invoke().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LoadStatus.Loading
    ).toCommonStateFlow()

    private val _newHistory = MutableStateFlow(null as History?)
    val newHistory = _newHistory.toCommonStateFlow()

    fun deleteHistory(historyId: Long) {
        viewModelScope.launch {
            deleteHistoryUseCase(historyId)
        }
    }

    fun createBasicHistory(title: String, text: String) {
        viewModelScope.launch {
            _newHistory.value = createBasicHistoryUseCase(title, text)
        }
    }

    fun onNewHistoryConsumed() {
        _newHistory.value = null
    }

    fun dispose() {
        viewModelScope.cancel()
    }
}

package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.model.domain.useCase.DeleteHistoryUseCase
import com.example.stories.model.domain.useCase.GetAllStoriesUseCase
import com.example.stories.model.domain.model.History
import kotlinx.coroutines.CoroutineScope
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
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    val storiesLoadStatus: CommonStateFlow<LoadStatus<List<History>>> = getAllStoriesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LoadStatus.Loading
    ).toCommonStateFlow()

    private val _newHistory = MutableStateFlow(null as History?)
    val newHistory = _newHistory.toCommonStateFlow()

    fun deleteHistory(historyId: String) {
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
}

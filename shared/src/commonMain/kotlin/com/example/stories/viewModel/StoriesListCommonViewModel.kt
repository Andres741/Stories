package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.toLoadStatusCommonStateFlow
import com.example.stories.model.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.model.domain.useCase.DeleteHistoryUseCase
import com.example.stories.model.domain.useCase.GetAllStoriesUseCase
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.get

class StoriesListCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getAllStoriesUseCase: GetAllStoriesUseCase = Component.get(),
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
    private val deleteHistoryUseCase: DeleteHistoryUseCase = Component.get(),
    private val createBasicHistoryUseCase: CreateBasicHistoryUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    val storiesLoadStatus: CommonStateFlow<LoadStatus<List<History>>> = getAllStoriesUseCase()
        .toLoadStatusCommonStateFlow(viewModelScope)

    private val _newHistory = MutableStateFlow(null as History?)
    val newHistory = _newHistory.toCommonStateFlow()

    private val _isLogged = MutableStateFlow(null as Boolean?)
    val isLogged = _isLogged.toCommonStateFlow()

    init {
        viewModelScope.launch {
            getLocalUserUseCase().collectLatest { user ->
                _isLogged.value = user != null
            }
        }
    }

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

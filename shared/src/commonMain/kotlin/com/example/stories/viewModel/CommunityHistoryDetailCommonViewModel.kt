package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.useCase.GetHistoryFromAPIUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get

class CommunityHistoryDetailCommonViewModel(
    historyId: String,
    userId: String,
    getHistoryFromAPIUseCase: GetHistoryFromAPIUseCase = Component.get(),
    coroutineScope: CoroutineScope?,
) : BaseCommonViewModel(coroutineScope) {

    private val _history = MutableStateFlow(LoadStatus.Loading as LoadStatus<History>)
    val history = _history.toCommonStateFlow()

    init {
        viewModelScope.launch {
            _history.value = getHistoryFromAPIUseCase(historyId, userId)
        }
    }
}

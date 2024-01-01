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

    private val _historyLoadStatus = MutableStateFlow(LoadStatus.Loading as LoadStatus<History>)
    val historyLoadStatus = _historyLoadStatus.toCommonStateFlow()

    constructor(historyId: String, userId: String): this(historyId = historyId, userId = userId, coroutineScope = null)

    init {
        viewModelScope.launch {
            _historyLoadStatus.value = getHistoryFromAPIUseCase(historyId, userId)
        }
    }
}

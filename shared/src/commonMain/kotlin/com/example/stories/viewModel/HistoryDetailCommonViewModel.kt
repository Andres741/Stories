package com.example.stories.viewModel

import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HistoryDetailCommonViewModel(
    historyId: Long,
    coroutineScope: CoroutineScope? = null,
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val getHistoryByIdUseCase: GetHistoryByIdUseCase = GetHistoryByIdUseCase()

    val historyLoadStatus: CommonStateFlow<LoadStatus<History>> = getHistoryByIdUseCase.invoke(historyId).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), LoadStatus.Loading
    ).toCommonStateFlow()

    fun dispose() {
        viewModelScope.cancel()
    }
}

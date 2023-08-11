package com.example.stories.viewModel

import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.EditHistoryUseCase
import com.example.stories.data.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HistoryDetailCommonViewModel(
    historyId: Long,
    coroutineScope: CoroutineScope? = null,
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val getHistoryByIdUseCase = GetHistoryByIdUseCase()
    private val editHistoryUseCase = EditHistoryUseCase()

    val historyLoadStatus: CommonStateFlow<LoadStatus<History>> = getHistoryByIdUseCase.invoke(historyId).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), LoadStatus.Loading
    ).toCommonStateFlow()

    private val _editingHistory = MutableStateFlow(null as History?)
    val editingHistory = _editingHistory.toCommonStateFlow()

    fun setEditMode() {
        _editingHistory.value = historyLoadStatus.value.dataOrNull()?.copy()
    }

    fun cancelEdit() {
        _editingHistory.value = null
    }

    fun saveItem(newElement: Element) {
        _editingHistory.update { oldHistory ->
            if (oldHistory == null) null
            else if (oldHistory.mainElement.id == newElement.id) oldHistory.copy(mainElement = newElement)
            else oldHistory.copy(
                elements = oldHistory.elements.map {
                    if (it.id == newElement.id) newElement
                    else it
                }
            )
        }
    }

    fun saveTitle(newTitle: String) {
        _editingHistory.update {
            it?.copy(title = newTitle)
        }
    }

    fun saveEditingHistory() {
        editHistoryUseCase(newHistory = _editingHistory.value ?: return)
        _editingHistory.value = null
    }

    fun dispose() {
        viewModelScope.cancel()
    }
}

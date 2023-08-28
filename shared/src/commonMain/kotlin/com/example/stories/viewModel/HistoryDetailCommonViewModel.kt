package com.example.stories.viewModel

import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.data.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryTitleUseCase
import com.example.stories.data.domain.useCase.EditHistoryUseCase
import com.example.stories.data.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.data.domain.useCase.CreateImageElementUseCase
import com.example.stories.data.domain.useCase.CreateTextElementUseCase
import com.example.stories.data.domain.useCase.DeleteElementUseCase
import com.example.stories.data.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.data.domain.useCase.SwapElementsUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.loading.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryDetailCommonViewModel(
    historyId: Long,
    coroutineScope: CoroutineScope? = null,
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val getHistoryByIdUseCase = GetHistoryByIdUseCase()
    private val editHistoryUseCase = EditHistoryUseCase()
    private val createEditingHistoryUseCase = CreateEditingHistoryUseCase()
    private val deleteEditingHistoryUseCase = DeleteEditingHistoryUseCase()
    private val updateHistoryTitleUseCase = UpdateHistoryTitleUseCase()
    private val updateHistoryElementUseCase = UpdateHistoryElementUseCase()
    private val updateHistoryDateRangeUseCase = UpdateHistoryDateRangeUseCase()
    private val createTextElementUseCase = CreateTextElementUseCase()
    private val createImageElementUseCase = CreateImageElementUseCase()
    private val swapElementsUseCase = SwapElementsUseCase()
    private val deleteElementUseCase = DeleteElementUseCase()

    val historyLoadStatus: CommonStateFlow<LoadStatus<History>> = getHistoryByIdUseCase.invoke(historyId).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), LoadStatus.Loading
    ).toCommonStateFlow()

    val editingHistory: CommonStateFlow<History?> = deleteEditingHistoryUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), null
    ).toCommonStateFlow()

    private val currentHistory get() = historyLoadStatus.value.dataOrNull()

    fun setEditMode() {
        createEditingHistoryUseCase(currentHistory?.id ?: return)
    }

    fun cancelEdit() {
        deleteEditingHistoryUseCase()
    }

    fun editElement(newElement: Element) {
        viewModelScope.launch {
            updateHistoryElementUseCase(newElement)
        }
    }

    fun editTitle(newTitle: String) {
        launchWithHistoryId { historyId ->
            updateHistoryTitleUseCase(historyId , newTitle)
        }
    }

    fun editDates(newDateRange: LocalDateRange) {
        launchWithHistoryId { historyId ->
            updateHistoryDateRangeUseCase(historyId, newDateRange)
        }
    }

    fun createTextElement(text: String) {
        launchWithHistoryId { historyId ->
            createTextElementUseCase(historyId, text)
        }
    }

    fun createImageElement(imageUrl: String) {
        launchWithHistoryId { historyId ->
            createImageElementUseCase(historyId, imageUrl)
        }
    }

    fun swapElements(fromId: Long, toId: Long) {
        launchWithHistoryId { historyId ->
            swapElementsUseCase(historyId, fromId, toId)
        }
    }

    fun deleteElement(element: Element) {
        viewModelScope.launch {
            deleteElementUseCase(element)
        }
    }

    fun saveEditingHistory() {
        editHistoryUseCase(newHistory = editingHistory.value ?: return)
        deleteEditingHistoryUseCase()
    }

    fun dispose() {
        viewModelScope.cancel()
    }

    private inline fun launchWithHistoryId(crossinline action: suspend (historyId: Long) -> Unit) {
        val historyId = currentHistory?.id ?: return
        viewModelScope.launch { action(historyId) }
    }
}

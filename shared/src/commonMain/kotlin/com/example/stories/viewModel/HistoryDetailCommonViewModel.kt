package com.example.stories.viewModel

import com.example.stories.Component
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
import org.koin.core.component.get

class HistoryDetailCommonViewModel(
    historyId: Long,
    coroutineScope: CoroutineScope?,
    getHistoryByIdUseCase: GetHistoryByIdUseCase = Component.get(),
    private val editHistoryUseCase: EditHistoryUseCase = Component.get(),
    private val createEditingHistoryUseCase: CreateEditingHistoryUseCase = Component.get(),
    private val deleteEditingHistoryUseCase: DeleteEditingHistoryUseCase = Component.get(),
    private val updateHistoryTitleUseCase: UpdateHistoryTitleUseCase = Component.get(),
    private val updateHistoryElementUseCase: UpdateHistoryElementUseCase = Component.get(),
    private val updateHistoryDateRangeUseCase: UpdateHistoryDateRangeUseCase = Component.get(),
    private val createTextElementUseCase: CreateTextElementUseCase = Component.get(),
    private val createImageElementUseCase: CreateImageElementUseCase = Component.get(),
    private val swapElementsUseCase: SwapElementsUseCase = Component.get(),
    private val deleteElementUseCase: DeleteElementUseCase = Component.get(),
) {

    constructor(historyId: Long): this(historyId = historyId, coroutineScope = null)

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

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

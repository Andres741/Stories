package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.useCase.CommitChangesUseCase
import com.example.stories.model.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.model.domain.useCase.CreateImageElementUseCase
import com.example.stories.model.domain.useCase.CreateTextElementUseCase
import com.example.stories.model.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.model.domain.useCase.DeleteElementUseCase
import com.example.stories.model.domain.useCase.GetEditingHistoryUseCase
import com.example.stories.model.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.model.domain.useCase.SwapElementsUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryTitleUseCase
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.get

class HistoryDetailCommonViewModel(
    private val historyId: String,
    coroutineScope: CoroutineScope?,
    getHistoryByIdUseCase: GetHistoryByIdUseCase = Component.get(),
    getEditingHistoryUseCase: GetEditingHistoryUseCase = Component.get(),
    private val createEditingHistoryUseCase: CreateEditingHistoryUseCase = Component.get(),
    private val deleteEditingHistoryUseCase: DeleteEditingHistoryUseCase = Component.get(),
    private val updateHistoryTitleUseCase: UpdateHistoryTitleUseCase = Component.get(),
    private val updateHistoryElementUseCase: UpdateHistoryElementUseCase = Component.get(),
    private val updateHistoryDateRangeUseCase: UpdateHistoryDateRangeUseCase = Component.get(),
    private val createTextElementUseCase: CreateTextElementUseCase = Component.get(),
    private val createImageElementUseCase: CreateImageElementUseCase = Component.get(),
    private val swapElementsUseCase: SwapElementsUseCase = Component.get(),
    private val deleteElementUseCase: DeleteElementUseCase = Component.get(),
    private val commitChangesUseCase: CommitChangesUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(historyId: String): this(historyId = historyId, coroutineScope = null)

    val historyLoadStatus: CommonStateFlow<LoadStatus<History>> = getHistoryByIdUseCase(historyId).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), LoadStatus.Loading
    ).toCommonStateFlow()

    val editingHistory: CommonStateFlow<History?> = getEditingHistoryUseCase(historyId).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(1000), null
    ).toCommonStateFlow()

    private val currentHistory get() = historyLoadStatus.value.dataOrNull()

    fun setEditMode() {
        viewModelScope.launch {
            createEditingHistoryUseCase(currentHistory?.id ?: return@launch)
        }
    }

    fun cancelEdit() {
        viewModelScope.launch {
            deleteEditingHistoryUseCase(historyId)
        }
    }

    fun editElement(newElement: HistoryElement) {
        viewModelScope.launch {
            updateHistoryElementUseCase(newElement)
        }
    }

    fun editTitle(newTitle: String) {
        viewModelScope.launch {
            updateHistoryTitleUseCase(historyId , newTitle)
        }
    }

    fun editDates(newDateRange: LocalDateRange) {
        viewModelScope.launch {
            updateHistoryDateRangeUseCase(historyId, newDateRange)
        }
    }

    fun createTextElement(text: String) {
        viewModelScope.launch {
            createTextElementUseCase(historyId, text)
        }
    }

    fun createImageElement(imageUrl: String) {
        viewModelScope.launch {
            createImageElementUseCase(historyId, imageUrl)
        }
    }

    fun swapElements(fromId: String, toId: String) {
        viewModelScope.launch {
            swapElementsUseCase(historyId, fromId, toId)
        }
    }

    fun deleteElement(element: HistoryElement) {
        viewModelScope.launch {
            deleteElementUseCase(element)
        }
    }

    fun saveEditingHistory() {
        viewModelScope.launch {
            commitChangesUseCase(historyId)
        }
    }
}

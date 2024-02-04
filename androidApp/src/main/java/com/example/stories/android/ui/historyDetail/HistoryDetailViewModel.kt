package com.example.stories.android.ui.historyDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.viewModel.HistoryDetailCommonViewModel

class HistoryDetailViewModel(historyId: String) : ViewModel() {

    private val commonViewModel = HistoryDetailCommonViewModel(
        historyId = historyId,
        coroutineScope = viewModelScope,
    )

    val historyLoadStatus = commonViewModel.historyLoadStatus
    val editingHistory = commonViewModel.editingHistory

    fun enableEditMode() = commonViewModel.setEditMode()

    fun cancelEdit() = commonViewModel.cancelEdit()

    fun editItem(element: HistoryElement) = commonViewModel.editElement(element)

    fun editTitle(newTitle: String) = commonViewModel.editTitle(newTitle)

    fun editDates(newDateRange: LocalDateRange) = commonViewModel.editDates(newDateRange)

    fun saveEditingHistory() = commonViewModel.saveEditingHistory()

    fun createTextElement(text: String) = commonViewModel.createTextElement(text)

    fun createImageElement(imageUrl: String) = commonViewModel.createImageElement(imageUrl)

    fun swapElements(fromId: String, toId: String) = commonViewModel.swapElements(fromId, toId)

    fun deleteElement(element: HistoryElement) = commonViewModel.deleteElement(element)

    class Factory(private val historyId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryDetailViewModel(historyId) as T
    }
}

package com.example.stories.android.ui.historyDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.data.domain.model.Element
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.viewModel.HistoryDetailCommonViewModel

class HistoryDetailViewModel(historyId: Long) : ViewModel() {

    private val commonViewModel = HistoryDetailCommonViewModel(
        historyId = historyId,
        coroutineScope = viewModelScope,
    )

    val historyLoadStatus = commonViewModel.historyLoadStatus
    val editingHistory = commonViewModel.editingHistory

    fun enableEditMode() = commonViewModel.setEditMode()

    fun cancelEdit() = commonViewModel.cancelEdit()

    fun editItem(element: Element) = commonViewModel.editItem(element)

    fun editTitle(newTitle: String) = commonViewModel.editTitle(newTitle)

    fun editDates(newDateRange: LocalDateRange) = commonViewModel.editDates(newDateRange)

    fun saveEditingHistory() = commonViewModel.saveEditingHistory()

    fun createTextElement(text: String) = commonViewModel.createTextElement(text)

    fun createImageElement(imageUrl: String) = commonViewModel.createImageElement(imageUrl)

    fun swapElements(fromId: Long, toId: Long) = commonViewModel.swapElements(fromId, toId)

    fun deleteElement(element: Element) = commonViewModel.deleteElement(element)

    class Factory(private val historyId: Long) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryDetailViewModel(historyId) as T
    }
}

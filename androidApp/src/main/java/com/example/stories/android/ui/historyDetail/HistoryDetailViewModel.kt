package com.example.stories.android.ui.historyDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.data.domain.model.Element
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

    fun saveItem(element: Element) = commonViewModel.saveItem(element)

    fun saveTitle(newTitle: String) = commonViewModel.saveTitle(newTitle)

    fun saveEditingHistory() = commonViewModel.saveEditingHistory()

    class Factory(private val historyId: Long) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryDetailViewModel(historyId) as T
    }
}

package com.example.stories.android.ui.historyDetail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.android.util.ImageUtils
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.viewModel.HistoryDetailCommonViewModel
import kotlinx.coroutines.launch

class HistoryDetailViewModel(historyId: String) : ViewModel() {

    private val commonViewModel = HistoryDetailCommonViewModel(
        historyId = historyId,
        coroutineScope = viewModelScope,
    )

    val historyLoadStatus = commonViewModel.historyLoadStatus
    val editingHistory = commonViewModel.editingHistory

    fun enableEditMode() = commonViewModel.setEditMode()

    fun cancelEdit() = commonViewModel.cancelEdit()

    fun editItem(context: Context, element: HistoryElement, imageUri: Uri?) {
        viewModelScope.launch {
            val updatedElement = imageUri?.let {
                ImageUtils.uriToImageDomain(imageUri, context)
            }?.let { imageData ->
                (element as? HistoryElement.Image)?.setDataFromUrl(imageData)
            } ?: element
            commonViewModel.editElement(updatedElement)
        }
    }

    fun editTitle(newTitle: String) = commonViewModel.editTitle(newTitle)

    fun editDates(newDateRange: LocalDateRange) = commonViewModel.editDates(newDateRange)

    fun saveEditingHistory() = commonViewModel.saveEditingHistory()

    fun createTextElement(text: String) = commonViewModel.createTextElement(text)

    fun createImageElement(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val imageData = ImageUtils.uriToImageDomain(imageUri, context) ?: return@launch
            commonViewModel.createImageElement(imageData)
        }
    }

    fun swapElements(fromId: String, toId: String) = commonViewModel.swapElements(fromId, toId)

    fun deleteElement(element: HistoryElement) = commonViewModel.deleteElement(element)

    class Factory(private val historyId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryDetailViewModel(historyId) as T
    }
}

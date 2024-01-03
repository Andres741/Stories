package com.example.stories.android.ui.storiesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.StoriesListCommonViewModel

class StoriesListViewModel : ViewModel() {

    private val commonViewModel = StoriesListCommonViewModel(viewModelScope)

    val storiesLoadStatus get() = commonViewModel.storiesLoadStatus
    val newHistory get() = commonViewModel.newHistory
    val isLogged get() = commonViewModel.isLogged

    fun deleteHistory(historyId: String) = commonViewModel.deleteHistory(historyId)
    fun createBasicHistory(title: String, text: String) = commonViewModel.createBasicHistory(title, text)
    fun onNewHistoryConsumed() = commonViewModel.onNewHistoryConsumed()
}

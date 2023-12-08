package com.example.stories.android.ui.storiesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.History
import com.example.stories.viewModel.StoriesListCommonViewModel

class StoriesListViewModel : ViewModel() {

    private val commonViewModel = StoriesListCommonViewModel(viewModelScope)

    val storiesLoadStatus: CommonStateFlow<LoadStatus<List<History>>> = commonViewModel.storiesLoadStatus
    val newHistory = commonViewModel.newHistory

    fun deleteHistory(historyId: String) = commonViewModel.deleteHistory(historyId)
    fun createBasicHistory(title: String, text: String) = commonViewModel.createBasicHistory(title, text)
    fun onNewHistoryConsumed() = commonViewModel.onNewHistoryConsumed()
}

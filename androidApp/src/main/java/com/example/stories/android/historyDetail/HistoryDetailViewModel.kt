package com.example.stories.android.historyDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.HistoryDetailCommonViewModel

class HistoryDetailViewModel(historyId: Long) : ViewModel() {

    private val commonViewModel = HistoryDetailCommonViewModel(historyId = historyId, coroutineScope = viewModelScope)

    val historyLoadStatus = commonViewModel.historyLoadStatus
}

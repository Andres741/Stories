package com.example.stories.android.ui.communityHistoryDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.CommunityHistoryDetailCommonViewModel

class CommunityHistoryDetailViewModel(historyId: String, userId: String) : ViewModel() {

    private val commonViewModel = CommunityHistoryDetailCommonViewModel(
        historyId = historyId,
        userId = userId,
        coroutineScope = viewModelScope,
    )

    val historyLoadStatus get() = commonViewModel.historyLoadStatus

    fun refreshData(showLoading: Boolean) = commonViewModel.refreshData(showLoading)

    class Factory(private val historyId: String, private val userId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CommunityHistoryDetailViewModel(
            historyId = historyId,
            userId = userId,
        ) as T
    }
}

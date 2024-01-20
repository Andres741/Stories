package com.example.stories.android.ui.communityStories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.CommunityStoriesListCommonViewModel

class CommunityStoriesListViewModel(userId: String) : ViewModel() {

    private val commonViewModel = CommunityStoriesListCommonViewModel(userId = userId, coroutineScope = viewModelScope)

    val userAndStoriesLoadStatus get() = commonViewModel.userAndStoriesLoadStatus

    fun refreshData() = commonViewModel.refreshData()

    class Factory(private val userId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CommunityStoriesListViewModel(userId) as T
    }
}

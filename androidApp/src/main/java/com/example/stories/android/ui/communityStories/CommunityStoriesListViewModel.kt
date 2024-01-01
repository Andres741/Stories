package com.example.stories.android.ui.communityStories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.CommunityStoriesListCommonViewModel

class CommunityStoriesListViewModel(userId: String) : ViewModel() {

    private val commonViewModel = CommunityStoriesListCommonViewModel(userId, viewModelScope)

    val userAndStoriesLoadStatus get() = commonViewModel.userAndStoriesLoadStatus

    class Factory(private val userId: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CommunityStoriesListViewModel(userId) as T
    }
}

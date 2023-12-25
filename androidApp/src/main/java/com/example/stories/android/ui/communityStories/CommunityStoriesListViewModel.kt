package com.example.stories.android.ui.communityStories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.CommunityStoriesListCommonViewModel

class CommunityStoriesListViewModel : ViewModel() {

    val commonViewModel = CommunityStoriesListCommonViewModel(viewModelScope)

    // TODO
}

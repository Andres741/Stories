package com.example.stories.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.CommunityCommonViewModel

class CommunityViewModel : ViewModel() {

    private val commonViewModel = CommunityCommonViewModel(coroutineScope = viewModelScope)

    val users get() = commonViewModel.users

    fun refreshData() = commonViewModel.refreshData()
}

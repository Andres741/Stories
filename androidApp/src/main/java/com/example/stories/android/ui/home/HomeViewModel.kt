package com.example.stories.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.HomeCommonViewModel

class HomeViewModel : ViewModel() {

    private val commonViewModel = HomeCommonViewModel(viewModelScope)

    val users = commonViewModel.users
}

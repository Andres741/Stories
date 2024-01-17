package com.example.stories.android.ui.userData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.UserDataCommonViewModel

class UserDataViewModel : ViewModel() {

    private val commonViewModel = UserDataCommonViewModel(viewModelScope)
    val userLoadStatus get() = commonViewModel.userLoadStatus
}

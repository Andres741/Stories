package com.example.stories.android.ui.logIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.LogInCommonViewModel

class LogInViewModel : ViewModel() {

    private val commonViewModel = LogInCommonViewModel(viewModelScope)

    val userCreationState get() = commonViewModel.userCreationState

    fun summitUserData(name: String, description: String, profileImage: String?) =
        commonViewModel.summitUserData(name, description, profileImage)
}

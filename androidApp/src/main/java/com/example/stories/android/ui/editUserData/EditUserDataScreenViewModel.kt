package com.example.stories.android.ui.editUserData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.viewModel.EditUserDataScreenCommonViewModel

class EditUserDataScreenViewModel : ViewModel() {

    private val commonViewModel = EditUserDataScreenCommonViewModel(viewModelScope)

    val localUserLoadStatus get() = commonViewModel.localUserLoadStatus
    val userCreationState get() = commonViewModel.userCreationState

    fun saveNewUserData(name: String, description: String, profileImage: String?) {
        commonViewModel.saveNewUserData(name, description, profileImage)
    }
}

package com.example.stories.android.ui.editUserData

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.android.util.ImageUtils
import com.example.stories.viewModel.EditUserDataScreenCommonViewModel
import kotlinx.coroutines.launch

class EditUserDataScreenViewModel : ViewModel() {

    private val commonViewModel = EditUserDataScreenCommonViewModel(viewModelScope)

    val localUserLoadStatus get() = commonViewModel.localUserLoadStatus
    val userCreationState get() = commonViewModel.userCreationState

    fun saveNewUserData(name: String, description: String, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            val image = imageUri?.let { ImageUtils.uriToImageDomain(imageUri, context) }
            commonViewModel.saveNewUserData(name, description, image)
        }
    }
}

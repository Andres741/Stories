package com.example.stories.android.ui.logIn

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.android.util.ImageUtils
import com.example.stories.viewModel.LogInCommonViewModel
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    private val commonViewModel = LogInCommonViewModel(viewModelScope)

    val userCreationState get() = commonViewModel.userCreationState

    fun summitUserData(name: String, description: String, profileImage: Uri?, context: Context) {
        viewModelScope.launch {
            val image = profileImage?.let { ImageUtils.uriToImageDomain(profileImage, context) }
            commonViewModel.summitUserData(name, description, image)
        }
    }
}

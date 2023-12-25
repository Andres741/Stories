package com.example.stories.android.ui.home

import androidx.lifecycle.ViewModel
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    // TODO: implement common view model

    val users: StateFlow<LoadStatus<List<User>>> = MutableStateFlow(LoadStatus.Data(emptyList()))
}

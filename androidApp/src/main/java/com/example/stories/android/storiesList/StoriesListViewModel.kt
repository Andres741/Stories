package com.example.stories.android.storiesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.viewModel.NoteListCommonViewModel

class StoriesListViewModel : ViewModel() {

    private val commonViewModel = NoteListCommonViewModel(viewModelScope)

    val stories: CommonStateFlow<List<History>> = commonViewModel.stories

}
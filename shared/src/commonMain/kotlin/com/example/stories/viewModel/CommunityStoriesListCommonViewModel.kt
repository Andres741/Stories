package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetUserStoriesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get

class CommunityStoriesListCommonViewModel(
    userId: String,
    coroutineScope: CoroutineScope? = null,
    getUserStoriesUseCase: GetUserStoriesUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(userId: String): this(userId = userId, coroutineScope = null)

    private val _userAndStories = MutableStateFlow(LoadStatus.Loading as LoadStatus<Pair<User, List<History>>>)
    val userAndStories = _userAndStories.toCommonStateFlow()

    init {
        viewModelScope.launch {
            _userAndStories.value = getUserStoriesUseCase(userId)
        }
    }
}

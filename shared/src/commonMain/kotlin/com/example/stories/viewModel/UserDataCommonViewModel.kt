package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.CommonStateFlow
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.get

class UserDataCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    val userLoadStatus: CommonStateFlow<LoadStatus<User>> = getLocalUserUseCase().map { user ->
        if (user != null) LoadStatus.Data(user)
        else LoadStatus.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoadStatus.Loading).toCommonStateFlow()
}

package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetAllUsersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get

class HomeCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getAllUsersUseCase: GetAllUsersUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    private val _users = MutableStateFlow(LoadStatus.Loading as LoadStatus<List<User>>)
    val users = _users.toCommonStateFlow()

    init {
        viewModelScope.launch {
            _users.value = getAllUsersUseCase()
        }
    }
}

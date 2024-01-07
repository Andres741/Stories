package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetAllUsersUseCase
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get

class CommunityCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getAllUsersUseCase: GetAllUsersUseCase = Component.get(),
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    private val _users = MutableStateFlow(LoadStatus.Loading as LoadStatus<List<User>>)
    val users = _users.toCommonStateFlow()

    init {
        viewModelScope.launch {
            val localUserFlow = getLocalUserUseCase()
            val allUsers = getAllUsersUseCase()

            localUserFlow.collect { localUser ->
                _users.value = if (localUser == null) allUsers else allUsers.mapData { users ->
                    users.filter { user ->
                        user.id != localUser.id
                    }
                }
            }
        }
    }
}

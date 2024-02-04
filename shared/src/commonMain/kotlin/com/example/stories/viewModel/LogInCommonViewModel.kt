package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.model.domain.useCase.CreateUserUseCase
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get

class LogInCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
    private val createUserUseCase: CreateUserUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    private val _userCreationState = MutableStateFlow(UserCreationState.None as UserCreationState)
    val userCreationState = _userCreationState.toCommonStateFlow()

    constructor(): this(coroutineScope = null)

    init {
        viewModelScope.launch {
            getLocalUserUseCase().collect { user ->
                if (user != null) {
                    _userCreationState.value = UserCreationState.Created
                }
            }
        }
    }

    fun summitUserData(name: String, description: String, profileImage: String?) {
        if (_userCreationState.value is UserCreationState.CreatingUser) return

        if (name.isBlank()) {
            _userCreationState.value = UserCreationState.NotValidName
            return
        }
        _userCreationState.value = UserCreationState.CreatingUser

        viewModelScope.launch {
            createUserUseCase(name, description, profileImage)
        }
    }
}

sealed interface UserCreationState {
    object None: UserCreationState
    object NotValidName: UserCreationState
    object CreatingUser: UserCreationState
    object Created: UserCreationState

    fun nextState() = when (this) {
        None -> NotValidName
        NotValidName -> CreatingUser
        CreatingUser -> Created
        Created -> None
    }
}

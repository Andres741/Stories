package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.base64ToByteArray
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import com.example.stories.model.domain.useCase.UpdateUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.get

class EditUserDataScreenCommonViewModel(
    coroutineScope: CoroutineScope? = null,
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
    private val updateUserUseCase: UpdateUserUseCase = Component.get(),
) : BaseCommonViewModel(coroutineScope) {

    private val _localUserLoadStatus = MutableStateFlow(LoadStatus.Loading as LoadStatus<User>)
    val localUserLoadStatus = _localUserLoadStatus.toCommonStateFlow()

    private val _userCreationState = MutableStateFlow(UserCreationState.None as UserCreationState)
    val userCreationState = _userCreationState.toCommonStateFlow()

    constructor(): this(coroutineScope = null)

    init {
        viewModelScope.launch {
            _localUserLoadStatus.value = LoadStatus.Data(getLocalUserUseCase().filterNotNull().first())
        }
    }

    fun saveNewUserData(name: String, description: String, imageData: ByteArray?) {
        val currentUser = localUserLoadStatus.value.dataOrNull() ?: return
        if (_userCreationState.value is UserCreationState.CreatingUser) return

        if (name.isBlank()) {
            _userCreationState.value = UserCreationState.NotValidName
            return
        }
        _userCreationState.value = UserCreationState.CreatingUser

        viewModelScope.launch {
            updateUserUseCase(
                user = currentUser.copy(name = name, description = description),
                imageData = imageData,
            )
            _userCreationState.value = UserCreationState.Created
        }
    }

    fun saveNewUserData(name: String, description: String, imageDataBase64: String?) {
        viewModelScope.launch {
            saveNewUserData(
                name = name,
                description = description,
                imageData = imageDataBase64?.base64ToByteArray(),
            )
        }
    }
}

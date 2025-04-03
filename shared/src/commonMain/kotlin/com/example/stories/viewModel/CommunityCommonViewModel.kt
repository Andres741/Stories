package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.infrastructure.loading.setRefreshing
import com.example.stories.infrastructure.loading.toLoadStatus
import com.example.stories.infrastructure.loading.toLoadStatusCommonStateFlow
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.useCase.GetAllUsersUseCase
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.get

class CommunityCommonViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase = Component.get(),
    getLocalUserUseCase: GetLocalUserUseCase = Component.get(),
    coroutineScope: CoroutineScope? = null,
) : BaseCommonViewModel(coroutineScope) {

    constructor(): this(coroutineScope = null)

    private val localUserFlow = getLocalUserUseCase()

    private val allUsersLoadStatus = MutableStateFlow(LoadStatus.Loading as LoadStatus<List<User>>)
    val users = combine(localUserFlow, allUsersLoadStatus) { localUser, allUsers ->
        if (localUser == null) allUsers else allUsers.mapData { users ->
            users.filter { user ->
                user.id != localUser.id
            }
        }
    }.toLoadStatusCommonStateFlow(viewModelScope)

    init {
        viewModelScope.launch {
            allUsersLoadStatus.value = getAllUsersUseCase().toLoadStatus()
        }
    }

    fun refreshData(showLoading: Boolean) {
        viewModelScope.launch {
            allUsersLoadStatus.setRefreshing(showLoading)
            allUsersLoadStatus.value = getAllUsersUseCase().toLoadStatus()
        }
    }
}

package com.example.stories.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseCommonViewModel(coroutineScope: CoroutineScope? = null) {

    protected val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    open fun dispose() {
        viewModelScope.cancel()
    }
}

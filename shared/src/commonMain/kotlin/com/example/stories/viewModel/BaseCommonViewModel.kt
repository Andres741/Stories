package com.example.stories.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus

abstract class BaseCommonViewModel(coroutineScope: CoroutineScope? = null) {

    val viewModelScope = coroutineScope?.let { it + Dispatchers.Default }
        ?: CoroutineScope(Dispatchers.Default)

    open fun dispose() {
        viewModelScope.cancel()
    }
}

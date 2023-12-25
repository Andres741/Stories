package com.example.stories.viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseCommonViewModel(coroutineScope: CoroutineScope? = null) {

    protected val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

}
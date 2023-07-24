package com.example.stories.infrastructure.coroutines.flow

import kotlinx.coroutines.flow.StateFlow

expect open class CommonStateFlow<T>(flow: StateFlow<T>) : StateFlow<T>

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)

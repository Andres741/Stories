package com.example.stories.infrastructure.coroutines.flow

import kotlinx.coroutines.flow.MutableStateFlow

actual open class CommonMutableStateFlow<T> actual constructor(
    private val flow: MutableStateFlow<T>
) : MutableStateFlow<T> by flow

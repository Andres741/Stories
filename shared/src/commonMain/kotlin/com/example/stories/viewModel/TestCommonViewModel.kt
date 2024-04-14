package com.example.stories.viewModel

import com.example.stories.Component
import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import com.example.stories.model.domain.useCase.SendImageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.get
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TestCommonViewModel(
    private val sendImageUseCase: SendImageUseCase = Component.get(),
    coroutineScope: CoroutineScope? = null,
) : BaseCommonViewModel(coroutineScope) {
    constructor(): this(coroutineScope = null)

    private val _imagesSent = MutableStateFlow(0)
    val imagesSent = _imagesSent.toCommonStateFlow()

    fun sendPhoto(image: ByteArray) {
        viewModelScope.launch {
            _imagesSent.value += sendImageUseCase(image).fold(
                ifLeft = { -1 },
                ifRight = { 1 },
            )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun sendPhoto(base64String: String) {
        viewModelScope.launch {
            sendPhoto(Base64.Default.decode(base64String))
        }
    }
}

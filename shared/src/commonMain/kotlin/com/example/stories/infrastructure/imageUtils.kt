package com.example.stories.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
suspend fun String.base64ToByteArray(): ByteArray = withContext(Dispatchers.Default) {
    Base64.Default.decode(this@base64ToByteArray)
}

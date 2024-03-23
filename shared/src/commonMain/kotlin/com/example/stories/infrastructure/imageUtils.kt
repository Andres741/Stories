package com.example.stories.infrastructure

import com.example.stories.model.dataSource.remote.image.ImagesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
suspend fun String.base64ToByteArray(): ByteArray = withContext(Dispatchers.Default) {
    Base64.Default.decode(this@base64ToByteArray)
}

fun String.toImageEndpoint() = "${ImagesApi.IMAGES_API}/$this.jpeg"

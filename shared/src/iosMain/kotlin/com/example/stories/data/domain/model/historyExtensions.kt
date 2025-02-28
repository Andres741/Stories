package com.example.stories.data.domain.model

import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.ImageResource
import com.example.stories.model.domain.model.toImageUrl
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

fun HistoryElement.Text.updateText(new: String) = copy(text = new)

fun HistoryElement.Image.updateUriImageResource(newUri: String) = copy(
    imageResource = ImageResource.ResourceImageUrl(newUri.toImageUrl()),
)

@OptIn(ExperimentalEncodingApi::class)
fun HistoryElement.Image.updateDataImageResource(newBase64Data: String) = copy(
    imageResource = ImageResource.ByteArrayImage(data = Base64.Default.decode(newBase64Data)),
)

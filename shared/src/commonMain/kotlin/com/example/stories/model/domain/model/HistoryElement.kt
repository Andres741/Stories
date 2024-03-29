package com.example.stories.model.domain.model

import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.ImageElementRealm
import com.example.stories.model.dataSource.local.history.model.TextElementRealm
import com.example.stories.model.dataSource.remote.history.model.HistoryElementResponse
import com.example.stories.model.dataSource.remote.history.model.HistoryImageResponse
import com.example.stories.model.dataSource.remote.history.model.HistoryTextResponse
import org.mongodb.kbson.BsonObjectId

sealed class HistoryElement {
    abstract val id: String

    data class Text(
        override val id: String,
        val text: String,
    ) : HistoryElement()
    data class Image(
        override val id: String,
        val imageResource: String,
    ) : HistoryElement()
}

fun HistoryElement.toRealm() = HistoryElementRealm().also { realmElement ->
    realmElement._id = BsonObjectId(hexString = id)
    when (this) {
        is HistoryElement.Image -> realmElement.image = ImageElementRealm().also { imageRealm ->
            imageRealm.imageResource = imageResource
        }
        is HistoryElement.Text -> realmElement.text = TextElementRealm().also { textRealm ->
            textRealm.text = text
        }
    }
}

fun HistoryElement.toResponse() = HistoryElementResponse(
    id = id,
    text = (this as? HistoryElement.Text)?.let { HistoryTextResponse(it.text) },
    image = (this as? HistoryElement.Image)?.let { HistoryImageResponse(it.imageResource) },
)

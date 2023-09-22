package com.example.stories.data.repository.history.model

import com.example.stories.data.localDataSource.history.model.HistoryElementRealm
import com.example.stories.data.localDataSource.history.model.ImageElementRealm
import com.example.stories.data.localDataSource.history.model.TextElementRealm
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

package com.example.stories.data.localDataSource.history.model

import com.example.stories.data.repository.history.model.HistoryElement
import io.realm.kotlin.types.EmbeddedRealmObject
import org.mongodb.kbson.ObjectId

class HistoryElementRealm : EmbeddedRealmObject {
    var _id: ObjectId = ObjectId()
    var text: TextElementRealm? = null
    var image: ImageElementRealm? = null
}

fun HistoryElementRealm.copy() = HistoryElementRealm().also {
    it._id = _id
    it.text = text
    it.image = image
}

class TextElementRealm : EmbeddedRealmObject {
    var text: String? = null
}

class ImageElementRealm : EmbeddedRealmObject {
    var imageResource: String? = null
}

fun HistoryElementRealm.toDomain(): HistoryElement? {
    return image?.let { image ->
        HistoryElement.Image(
            id = _id.toHexString(),
            imageResource = image.imageResource ?: return null
        )
    } ?: text?.let { text ->
        HistoryElement.Text(
            id = _id.toHexString(),
            text = text.text ?: return null
        )
    }
}

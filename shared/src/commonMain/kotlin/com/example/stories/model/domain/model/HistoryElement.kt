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
        val imageResource: ImageResource,
    ) : HistoryElement() {
        fun setDataFromUrl(data: ByteArray): Image {
            return copy(imageResource = ImageResource.ByteArrayImage(data))
        }
    }

    fun getImageData(): ByteArray? {
        val imageResource = (this as? Image)?.imageResource ?: return null
        return (imageResource as? ImageResource.ByteArrayImage)?.data
    }
}

sealed interface ImageResource {
    data class ByteArrayImage(
        val data: ByteArray,
    ) : ImageResource {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ByteArrayImage) return false

            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class ImageUrl(val url: String) : ImageResource

    val model get() = (this as? ByteArrayImage)?.data ?: (this as? ImageUrl)?.url
}

fun HistoryElement.toRealm() = HistoryElementRealm().also { realmElement ->
    realmElement._id = BsonObjectId(hexString = id)
    when (this) {
        is HistoryElement.Image -> realmElement.image = ImageElementRealm().also { imageRealm ->
            imageRealm.imageResource = (imageResource as? ImageResource.ByteArrayImage)?.data
        }
        is HistoryElement.Text -> realmElement.text = TextElementRealm().also { textRealm ->
            textRealm.text = text
        }
    }
}

fun HistoryElement.toResponse(idToImageName: Map<String, String?> = emptyMap()) = HistoryElementResponse(
    id = id,
    text = (this as? HistoryElement.Text)?.let { HistoryTextResponse(it.text) },
    image = (this as? HistoryElement.Image)?.let {
        HistoryImageResponse(
            imageUrl = (it.imageResource as? ImageResource.ImageUrl)?.url ?: idToImageName[id] ?: "",
        )
    },
)

package com.example.stories.model.domain.model

import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.ImageElementRealm
import com.example.stories.model.dataSource.local.history.model.TextElementRealm
import com.example.stories.model.dataSource.remote.history.model.HistoryElementResponse
import com.example.stories.model.dataSource.remote.history.model.HistoryImageResponse
import com.example.stories.model.dataSource.remote.history.model.HistoryTextResponse
import org.mongodb.kbson.BsonObjectId
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
        fun updateImageResource(new: String): Image {
            return copy(imageResource = ImageResource.ResourceImageUrl(ImageUrl(new)))
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

        @OptIn(ExperimentalEncodingApi::class)
        val base64Data by lazy { Base64.Default.encode(data) }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ByteArrayImage) return false

            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class ResourceImageUrl(val imageUrl: ImageUrl) : ImageResource

    val model get(): Any = when (this) {
        is ByteArrayImage -> data
        is ResourceImageUrl -> imageUrl.url
    }
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

fun HistoryElement.toResponse(idToImage: Map<String, ImageUrl> = emptyMap()) = HistoryElementResponse(
    id = id,
    text = (this as? HistoryElement.Text)?.let { HistoryTextResponse(it.text) },
    image = (this as? HistoryElement.Image)?.let {
        val imageUrl = (it.imageResource as? ImageResource.ResourceImageUrl)?.imageUrl ?: idToImage[id]
        HistoryImageResponse(
            imageName = imageUrl?.imageName ?: "",
        )
    },
)

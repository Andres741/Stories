package com.example.stories.data.domain.model

import kotlinx.datetime.LocalDateTime

data class History(
    val id: Long,
    val title: String,
    val mainImage: String?,
    val date: LocalDateTime,
    val elements: List<Element>
)

sealed class Element {
    abstract val id: String

    data class Text(
        val text: String
    ) : Element() {
        override val id get() = text
    }
    data class Image(
        val imageResource: String
    ) : Element() {
        override val id get() = imageResource
    }

}

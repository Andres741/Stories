package com.example.stories.data.domain.model

import kotlinx.datetime.LocalDateTime

data class History(
    val id: Long,
    val title: String,
    val date: LocalDateTime,
    val mainElement: Element,
    val elements: List<Element>,
)

sealed class Element {
    abstract val id: Long

    data class Text(
        override val id: Long,
        val text: String,
    ) : Element()
    data class Image(
        override val id: Long,
        val imageResource: String,
    ) : Element()
}

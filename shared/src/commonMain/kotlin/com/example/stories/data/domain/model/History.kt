package com.example.stories.data.domain.model

import com.example.stories.infrastructure.date.LocalDateRange

data class History(
    val id: Long,
    val title: String,
    val dateRange: LocalDateRange,
    val elements: List<Element>,
) {
    val mainElement get() = elements.first()
}

sealed interface Element {
    val id: Long

    data class Text(
        override val id: Long,
        val text: String,
    ) : Element
    data class Image(
        override val id: Long,
        val imageResource: String,
    ) : Element
}

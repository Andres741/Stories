package com.example.stories.data.repository.history.model

sealed class HistoryElement {
    abstract val id: Long

    data class Text(
        override val id: Long,
        val text: String,
    ) : HistoryElement()
    data class Image(
        override val id: Long,
        val imageResource: String,
    ) : HistoryElement()
}

package com.example.stories.model.dataSource.remote.history.model

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import kotlinx.serialization.Serializable

@Serializable
data class HistoryResponse(
    val id: String,
    val title: String,
    val startDate: Long,
    val endDate: Long?,
    val elements: List<HistoryElementResponse>,
)

fun HistoryResponse.toDomain() = History(
    id = id,
    title = title,
    dateRange = LocalDateRange.from(startDate, endDate),
    elements = elements.mapNotNull { it.toDomain() }
)

fun List<HistoryResponse>.toDomain() = map(HistoryResponse::toDomain)

@Serializable
data class HistoryElementResponse(
    val id: String,
    val text: HistoryTextResponse?,
    val image: HistoryImageResponse?,
)

fun HistoryElementResponse.toDomain() = text?.let {
    HistoryElement.Text(
        id = id,
        text = it.text
    )
} ?: image?.let {
    HistoryElement.Image(
        id = id,
        imageResource = it.imageUrl,
    )
}

@Serializable
data class HistoryTextResponse(
    val text: String,
)
@Serializable
data class HistoryImageResponse(
    val imageUrl: String,
)

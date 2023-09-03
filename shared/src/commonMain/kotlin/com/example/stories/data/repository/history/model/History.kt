package com.example.stories.data.repository.history.model

import com.example.stories.infrastructure.date.LocalDateRange

data class History(
    val id: Long,
    val title: String,
    val dateRange: LocalDateRange,
    val elements: List<HistoryElement>,
) {
    val mainElement get() = elements.first()
}

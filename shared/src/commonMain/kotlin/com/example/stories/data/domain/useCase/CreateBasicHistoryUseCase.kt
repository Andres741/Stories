package com.example.stories.data.domain.useCase

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.model.HistoryMocks
import com.example.stories.data.repository.history.model.HistoryElement
import com.example.stories.data.repository.history.model.History
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.now
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate


class CreateBasicHistoryUseCase(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(title: String, text: String): History {
        return History(
            id = HistoryMocks.id++,
            title = title,
            dateRange = LocalDateRange.create(LocalDate.now(), null),
            elements = listOf(
                HistoryElement.Text(
                    id = HistoryMocks.id++,
                    text = text,
                )
            )
        ).also { newHistory ->
            GetAllStoriesUseCase.mockStoriesFlow.update {
                it + newHistory
            }
        }
    }
}

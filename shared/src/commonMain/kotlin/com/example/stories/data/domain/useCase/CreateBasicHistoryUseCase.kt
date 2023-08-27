package com.example.stories.data.domain.useCase

import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.now
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate


class CreateBasicHistoryUseCase {

    suspend operator fun invoke(title: String, text: String): History {
        return History(
            id = Mocks.id++,
            title = title,
            dateRange = LocalDateRange.create(LocalDate.now(), null),
            elements = listOf(
                Element.Text(
                    id = Mocks.id++,
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

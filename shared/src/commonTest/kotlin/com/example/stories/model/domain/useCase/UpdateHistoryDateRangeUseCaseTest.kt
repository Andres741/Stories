package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toLocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateHistoryDateRangeUseCaseTest {

    @Test
    fun `invoke should call repository updateHistoryDateRange with correct parameters`() = runBlocking {
        // Given
        val historyIdExpected = "hist-date-range-123"
        val dateRangeExpected = LocalDateRange(
            startDate = "2023-01-15".toLocalDate(),
            endDate = "2023-02-20".toLocalDate()
        )

        val updateDateRangeInvocation = InvocationCounter(1)
        var capturedHistoryId: String? = null
        var capturedDateRange: LocalDateRange? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun updateHistoryDateRange(historyId: String, newDateRange: LocalDateRange) {
                updateDateRangeInvocation()
                capturedHistoryId = historyId
                capturedDateRange = newDateRange
            }
        }

        val useCase = UpdateHistoryDateRangeUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(historyIdExpected, dateRangeExpected)

        // Then
        updateDateRangeInvocation.verifyInvocations()
        assertEquals(historyIdExpected, capturedHistoryId)
        assertEquals(dateRangeExpected, capturedDateRange)
    }
}

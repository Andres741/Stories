package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.ImageResource
import com.example.stories.model.domain.model.ImageUrl
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateHistoryElementUseCaseTest {

    @Test
    fun `invoke with TextElement should call repository updateHistoryElement`() = runBlocking {
        // Given
        val textElementExpected = HistoryElement.Text(
            id = "text-elem-1",
            text = "Updated text"
        )
        val updateElementInvocation = InvocationCounter(1)
        var capturedElement: HistoryElement? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun updateHistoryElement(historyElement: HistoryElement) {
                updateElementInvocation()
                capturedElement = historyElement
            }
        }

        val useCase = UpdateHistoryElementUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(textElementExpected)

        // Then
        updateElementInvocation.verifyInvocations()
        assertEquals(textElementExpected, capturedElement)
    }

    @Test
    fun `invoke with ImageElement should call repository updateHistoryElement`() = runBlocking {
        // Given
        val imageElementExpected = HistoryElement.Image(
            id = "image-elem-2",
            imageResource = ImageResource.ResourceImageUrl(ImageUrl("http://example.com/updated.jpg")),
        )
        val updateElementInvocation = InvocationCounter(1)
        var capturedElement: HistoryElement? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun updateHistoryElement(historyElement: HistoryElement) {
                updateElementInvocation()
                capturedElement = historyElement
            }
        }

        val useCase = UpdateHistoryElementUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(imageElementExpected)

        // Then
        updateElementInvocation.verifyInvocations()
        assertEquals(imageElementExpected, capturedElement)
    }
}

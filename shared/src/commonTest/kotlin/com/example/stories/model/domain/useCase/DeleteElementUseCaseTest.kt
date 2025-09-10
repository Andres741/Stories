package com.example.stories.model.domain.useCase

import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.repository.DefaultFakeHistoryRepository
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.testUtil.InvocationCounter
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteElementUseCaseTest {

    @Test
    fun `invoke should call repository deleteEditingElement with correct element id`() = runBlocking {
        // Given
        val elementIdExpected = "element-to-delete-789"
        val mockElement = HistoryElement.Text(id = elementIdExpected, text = "Some text")

        val deleteEditingElementInvocation = InvocationCounter(1)
        var capturedElementId: String? = null

        val fakeHistoryRepository: HistoryRepository = object : DefaultFakeHistoryRepository() {
            override suspend fun deleteEditingElement(elementId: String) {
                deleteEditingElementInvocation()
                capturedElementId = elementId
            }
        }

        val useCase = DeleteElementUseCase(
            historyRepository = fakeHistoryRepository
        )

        // When
        useCase(mockElement)

        // Then
        deleteEditingElementInvocation.verifyInvocations()
        assertEquals(elementIdExpected, capturedElementId)
    }
}

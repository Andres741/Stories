package com.example.stories.android.ui.historyDetail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditDatePopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditImageElementPopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditTextElementPopUp
import com.example.stories.android.util.ui.AsyncItemImage
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.format
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ElementsListBody(
    history: History,
    editMode: Boolean,
    rotation: () -> Float,
    onEditElement: (Element) -> Unit,
    onEditDateRange: (LocalDateRange) -> Unit,
    swapElements: (fromId: Long, toId: Long) -> Unit,
) {

    var editingElement by remember { mutableStateOf(null as Element?) }

    editingElement?.let { element ->
        when (element) {
            is Element.Text -> EditTextElementPopUp(
                text = element.text,
                onConfirm = {
                    onEditElement(element.copy(text = it))
                    editingElement = null
                },
                onDismiss = { editingElement = null }
            )
            is Element.Image -> EditImageElementPopUp(
                imageUrl = element.imageResource,
                onConfirm = {
                    onEditElement(element.copy(imageResource = it))
                    editingElement = null
                },
                onDismiss = { editingElement = null }
            )
        }
    }

    val state = rememberReorderableLazyListState(
        onMove = swap@{ from, to ->
            swapElements(
                from.key as? Long ?: return@swap,
                to.key as? Long ?: return@swap,
            )
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item(history.mainElement.id) {
            ElementItem(
                historyElement = history.mainElement,
                modifier = Modifier
                    .clickable(enabled = editMode) { editingElement = history.mainElement }
                    .graphicsLayer { rotationZ = rotation() }
            )
        }

        items(history.elements, key = { it.id }) { historyElement ->
            ElementItem(
                historyElement = historyElement,
                modifier = Modifier
                    .clickable(enabled = editMode) { editingElement = historyElement }
                    .graphicsLayer { rotationZ = rotation() }
            )
        }

        item {
            DateRangeFooter(history, editMode, rotation, onEditDateRange)
        }
    }
}

@Composable
fun ElementItem(
    historyElement: Element,
    modifier: Modifier = Modifier,
) {
    ItemCard(modifier = modifier) {
        when(historyElement) {
            is Element.Image -> ElementImageItem(historyElement)
            is Element.Text -> ElementTextItem(historyElement)
        }
    }
}

@Composable
fun ElementImageItem(image: Element.Image) {
    AsyncItemImage(image.imageResource)
}

@Composable
fun ElementTextItem(text: Element.Text) {
    Text(
        text = text.text,
        modifier = Modifier
            .fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun DateRangeFooter(
    history: History,
    editMode: Boolean,
    rotation: () -> Float,
    onEditDateRange: (LocalDateRange) -> Unit
) {

    var editingDateRange by remember { mutableStateOf(null as LocalDateRange?) }
    editingDateRange?.let {
        EditDatePopUp(
            dateRange = it,
            onConfirm = { newDateRange ->
                onEditDateRange(newDateRange)
                editingDateRange = null
            },
            onDismiss = {
                editingDateRange = null
            }
        )
    }

    val color = animateColorAsState(
        targetValue =
        if (editMode) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.background,
    ).run { { value } }

    val topOffset = animateDpAsState(
        targetValue = if (editMode) 6.dp else 0.dp,
    ).run { { value } }

    val dateShape = MaterialTheme.shapes.small

    Box(
        modifier = Modifier.height(100.dp)
    ) {
        Text(
            text = history.dateRange.format(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation()
                    translationY = topOffset().toPx()
                }
                .drawBehind {
                    drawOutline(dateShape.createOutline(size, layoutDirection, this), color())
                }
                .clickable(enabled = editMode) { editingDateRange = history.dateRange }
                .padding(vertical = 5.dp)
        )
    }
}

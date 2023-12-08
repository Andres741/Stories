package com.example.stories.android.ui.historyDetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditDatePopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditImageElementPopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditTextElementPopUp
import com.example.stories.android.util.ui.AsyncItemImage
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.format
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ElementsListBody(
    history: History,
    editMode: Boolean,
    rotation: () -> Float,
    onEditElement: (HistoryElement) -> Unit,
    onEditDateRange: (LocalDateRange) -> Unit,
    swapElements: (fromId: String, toId: String) -> Unit,
    deleteElement: (element: HistoryElement) -> Unit,
) {

    var editingElement by remember { mutableStateOf(null as HistoryElement?) }

    editingElement?.let { element ->
        when (element) {
            is HistoryElement.Text -> EditTextElementPopUp(
                text = element.text,
                onConfirm = {
                    onEditElement(element.copy(text = it))
                    editingElement = null
                },
                onDismiss = { editingElement = null }
            )
            is HistoryElement.Image -> EditImageElementPopUp(
                imageUrl = element.imageResource,
                onConfirm = {
                    onEditElement(element.copy(imageResource = it))
                    editingElement = null
                },
                onDismiss = { editingElement = null }
            )
        }
    }

    val numElements = history.elements.size

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(history.elements, key = { _, item -> item.id }) { index, historyElement ->
            ElementItem(
                historyElement = historyElement,
                editMode = editMode,
                modifier = Modifier
                    .animateItemPlacement()
                    .clickable(enabled = editMode) { editingElement = historyElement }
                    .graphicsLayer { rotationZ = rotation() },
                moveElementUp = history.elements.getOrNull(index - 1)?.let { prevElement ->
                    { swapElements(historyElement.id, prevElement.id) }
                },
                moveElementDown = history.elements.getOrNull(index + 1)?.let { nextElement ->
                    { swapElements(historyElement.id, nextElement.id) }
                },
                deleteElement = deleteElement.takeIf { numElements > 1 }
            )
        }

        item {
            DateRangeFooter(history, editMode, rotation, onEditDateRange)
        }
    }
}

@Composable
fun ElementItem(
    historyElement: HistoryElement,
    editMode: Boolean,
    modifier: Modifier = Modifier,
    moveElementUp: (() -> Unit)?,
    moveElementDown: (() -> Unit)?,
    deleteElement: ((element: HistoryElement) -> Unit)?,
) {
    ItemCard(modifier = modifier) {
        when(historyElement) {
            is HistoryElement.Image -> ElementImageItem(historyElement)
            is HistoryElement.Text -> ElementTextItem(historyElement)
        }
        AnimatedVisibility(visible = editMode) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.align(Alignment.Center)) {
                    IconButton(onClick = moveElementUp ?: {}, enabled = moveElementUp != null) {
                        Icon(Icons.Filled.ArrowForward, "", Modifier.rotate(-90f))
                    }
                    IconButton(onClick = moveElementDown ?: {}, enabled = moveElementDown != null) {
                        Icon(Icons.Filled.ArrowForward, "", Modifier.rotate(90f))
                    }
                }
                IconButton(
                    onClick = { deleteElement?.invoke(historyElement) },
                    enabled = deleteElement != null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(Icons.Filled.Delete, "")
                }
            }
        }
    }
}

@Composable
fun ElementImageItem(image: HistoryElement.Image) {
    AsyncItemImage(image.imageResource)
}

@Composable
fun ElementTextItem(text: HistoryElement.Text) {
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
        label = "",
    ).run { { value } }

    val topOffset = animateDpAsState(
        targetValue = if (editMode) 6.dp else 0.dp,
        label = "",
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

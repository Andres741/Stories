package com.example.stories.android.ui.historyDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.historyDetail.editPopUp.EditDatePopUp
import com.example.stories.android.ui.historyDetail.editPopUp.EditElementPopUp
import com.example.stories.android.ui.historyDetail.editPopUp.EditTitlePopUp
import com.example.stories.android.util.resources.sharedPainterResource
import com.example.stories.android.util.ui.AsyncItemImage
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.android.util.ui.actionableFloatAnimation
import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.format

@Composable
fun HistoryDetailScreen(
    viewModel: HistoryDetailViewModel
) {
    val historyLoadStatus by viewModel.historyLoadStatus.collectAsStateWithLifecycle()

    LoadingDataScreen(loadStatus = historyLoadStatus) { history ->
        val editingItem by viewModel.editingHistory.collectAsStateWithLifecycle()
        val editMode = editingItem != null
        HistoryDetail(
            history = editingItem ?: history,
            editMode = editMode,
            saveElement = viewModel::saveItem,
            saveTitle = viewModel::saveTitle,
            saveDateRange = viewModel::saveDates,
            inverseEditHistory =
                if (editMode) viewModel::cancelEdit
                else viewModel::enableEditMode,
            saveEditingHistory = viewModel::saveEditingHistory,
        )
    }
}

@Composable
fun HistoryDetail(
    history: History,
    editMode: Boolean,
    saveElement: (Element) -> Unit,
    saveTitle: (newTitle: String) -> Unit,
    saveDateRange: (newDateRange: LocalDateRange) -> Unit,
    inverseEditHistory: () -> Unit,
    saveEditingHistory: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            Row {
                AnimatedVisibility(visible = editMode) {
                    FloatingActionButton(onClick = saveEditingHistory) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            painter = sharedPainterResource { save_icon },
                            contentDescription = "",
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(onClick = inverseEditHistory) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = sharedPainterResource { if (editMode) cancel_icon else edit_icon },
                        contentDescription = "",
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            val rotation = actionableFloatAnimation(
                isActive = editMode,
                disabledValue = 0f,
                values = remember { listOf(.4f, -.4f) },
                animationSpec = tween(
                    durationMillis = 60,
                )
            ).run { { value } }

            var showEditTitlePopUp by remember { mutableStateOf(false) }

            if (showEditTitlePopUp) {
                EditTitlePopUp(
                    title = history.title,
                    onConfirm = {
                        saveTitle(it)
                        showEditTitlePopUp = false
                    },
                    onDismiss = { showEditTitlePopUp = false }
                )
            }

            Text(
                text = history.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 24.dp)
                    .graphicsLayer { rotationZ = rotation() }
                    .clickable(enabled = editMode) { showEditTitlePopUp = true },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )

            var editingElement by remember { mutableStateOf(null as Element?) }

            editingElement?.let {
                EditElementPopUp(
                    historyElement = it,
                    onConfirm = { element ->
                        saveElement(element)
                        editingElement = null
                    },
                    onDismiss = {
                        editingElement = null
                    }
                )
            }

            var editingDateRange by remember { mutableStateOf(null as LocalDateRange?) }

            editingDateRange?.let {
                EditDatePopUp(
                    dateRange = it,
                    onConfirm = { newDateRange ->
                        saveDateRange(newDateRange)
                        editingDateRange = null
                    },
                    onDismiss = {
                        editingDateRange = null
                    }
                )
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                history.elements.forEach { historyElement ->
                    item(key = historyElement.id ) {
                        ElementItem(
                            historyElement = historyElement,
                            modifier = Modifier
                                .clickable(enabled = editMode) { editingElement = historyElement }
                                .graphicsLayer { rotationZ = rotation() }
                        )
                    }
                }
                item {
                    val color = animateColorAsState(
                        targetValue =
                            if (editMode) MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.background,
                    ).run { { value } }

                    val topOffset = animateDpAsState(
                        targetValue = if (editMode) 6.dp else 0.dp,
                    ).run { { value } }

                    val dateShape = MaterialTheme.shapes.small

                    Box(modifier = Modifier.height(65.dp)) {
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
            }
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

@Preview
@Composable
fun StoriesList_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var editMode by remember { mutableStateOf(false) }
            HistoryDetail(
                history = if (editMode) Mocks().getMockStories()[2] else Mocks().getMockStories()[1],
                editMode = editMode,
                saveElement = {},
                saveTitle = {},
                saveDateRange = {},
                inverseEditHistory = { editMode = !editMode },
                saveEditingHistory = { editMode = false}
            )
        }
    }
}

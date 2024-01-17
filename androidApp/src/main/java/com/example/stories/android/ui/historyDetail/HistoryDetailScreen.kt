package com.example.stories.android.ui.historyDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.historyDetail.components.AddElementFooter
import com.example.stories.android.ui.components.ElementsListBody
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditDatePopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditImageElementPopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditTextElementPopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditTitlePopUp
import com.example.stories.android.util.resources.getPainterResource
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.android.util.ui.actionableFloatAnimation
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.HistoryMocks

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
            editElement = viewModel::editItem,
            editTitle = viewModel::editTitle,
            editDateRange = viewModel::editDates,
            inverseEditHistory =
                if (editMode) viewModel::cancelEdit
                else viewModel::enableEditMode,
            saveEditingHistory = viewModel::saveEditingHistory,
            createTextElement = viewModel::createTextElement,
            createImageElement = viewModel::createImageElement,
            swapElements = viewModel::swapElements,
            deleteElement = viewModel::deleteElement
        )
    }
}

@Composable
fun HistoryDetail(
    history: History,
    editMode: Boolean,
    editElement: (HistoryElement) -> Unit,
    editTitle: (newTitle: String) -> Unit,
    editDateRange: (newDateRange: LocalDateRange) -> Unit,
    inverseEditHistory: () -> Unit,
    saveEditingHistory: () -> Unit,
    createTextElement: (text: String) -> Unit,
    createImageElement: (imageUrl: String) -> Unit,
    swapElements: (fromId: String, toId: String) -> Unit,
    deleteElement: (element: HistoryElement) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            HistoryDetailFab(editMode, inverseEditHistory, saveEditingHistory)
        }
    ) { padding ->
        Box(
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

            var editingElement by remember { mutableStateOf(null as HistoryElement?) }
            editingElement?.let { element ->
                when (element) {
                    is HistoryElement.Text -> EditTextElementPopUp(
                        text = element.text,
                        onConfirm = {
                            editElement(element.copy(text = it))
                            editingElement = null
                        },
                        onDismiss = { editingElement = null }
                    )
                    is HistoryElement.Image -> EditImageElementPopUp(
                        imageUrl = element.imageResource,
                        onConfirm = {
                            editElement(element.copy(imageResource = it))
                            editingElement = null
                        },
                        onDismiss = { editingElement = null }
                    )
                }
            }

            var editingDateRange by remember { mutableStateOf(null as LocalDateRange?) }
            editingDateRange?.let {
                EditDatePopUp(
                    dateRange = it,
                    onConfirm = { newDateRange ->
                        editDateRange(newDateRange)
                        editingDateRange = null
                    },
                    onDismiss = {
                        editingDateRange = null
                    }
                )
            }

            Column(modifier = Modifier.align(Alignment.TopCenter)) {
                TitleHeader(
                    title = history.title,
                    editMode = editMode,
                    rotation = rotation,
                    onEditTitle = editTitle,
                )

                ElementsListBody(
                    history = history,
                    editMode = editMode,
                    rotation = rotation,
                    onClickElement = { editingElement = it },
                    onClickDate = { editingDateRange = it },
                    swapElements = swapElements,
                    deleteElement = deleteElement,
                )
            }

            AddElementFooter(
                editMode = editMode,
                modifier = Modifier.align(Alignment.BottomCenter),
                createTextElement = createTextElement,
                createImageElement = createImageElement,
            )
        }
    }
}

@Composable
private fun TitleHeader(
    title: String,
    editMode: Boolean,
    rotation: () -> Float,
    onEditTitle: (String) -> Unit,
) {

    var editingTitle by remember { mutableStateOf(null as String?) }

    editingTitle?.let { editing ->
        EditTitlePopUp(
            title = editing,
            onConfirm = {
                onEditTitle(it)
                editingTitle = null
            },
            onDismiss = { editingTitle = null }
        )
    }

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 24.dp)
            .graphicsLayer { rotationZ = rotation() }
            .clickable(enabled = editMode) { editingTitle = title },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayMedium
    )
}

@Composable
fun HistoryDetailFab(
    editMode: Boolean,
    inverseEditHistory: () -> Unit,
    saveEditingHistory: () -> Unit,
) {
    Row {
        AnimatedVisibility(visible = editMode) {
            FloatingActionButton(onClick = saveEditingHistory) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = getPainterResource { save_icon },
                    contentDescription = "",
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(onClick = inverseEditHistory) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = getPainterResource { if (editMode) cancel_icon else edit_icon },
                contentDescription = "",
            )
        }
    }
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
                history = if (editMode) HistoryMocks().getMockStories()[2] else HistoryMocks().getMockStories()[1],
                editMode = editMode,
                editElement = {},
                editTitle = {},
                editDateRange = {},
                inverseEditHistory = { editMode = !editMode },
                saveEditingHistory = { editMode = false},
                createTextElement = {},
                createImageElement = {},
                swapElements = { _, _ -> },
                deleteElement = {}
            )
        }
    }
}

package com.example.stories.android.ui.storiesList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.AsyncItemImage
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.format

@Composable
fun StoriesListScreen(
    viewModel: StoriesListViewModel,
    navigateDetail: (Long) -> Unit,
) {
    val storiesLoadStatus by viewModel.storiesLoadStatus.collectAsStateWithLifecycle()
    LoadingDataScreen(storiesLoadStatus) { stories ->
        StoriesList(
            stories = stories,
            onClickItem = navigateDetail,
            deleteHistory = viewModel::deleteHistory,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoriesList(
    stories: List<History>,
    onClickItem: (Long) -> Unit,
    deleteHistory: (Long) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = getStringResource { stories_screen_title },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )

            var deletingHistoryId by remember { mutableStateOf(null as Long?) }

            deletingHistoryId?.let { id ->
                AlertDialog(
                    onDismissRequest = { deletingHistoryId = null },
                    title = { Text(getStringResource { delete_history_pop_up_title }) },
                    text = { Text(getStringResource { delete_history_pop_up_text }) },
                    confirmButton = {
                        TextButton(onClick = { deleteHistory(id); deletingHistoryId = null }) {
                            Text(getStringResource { accept })
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { deletingHistoryId = null }) {
                            Text(getStringResource { dismiss })
                        }
                    },
                )
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(stories, key = { it.id }) {history ->
                    HistoryItem(
                        history = history,
                        onClickItem = onClickItem,
                        onClickDelete = { deletingHistoryId = it },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    history: History,
    onClickItem: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ItemCard(
        modifier = modifier.clickable { onClickItem(history.id) }
    ) {

        Text(
            text = history.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 6.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 6.dp)
        ) {
            when (val mainItem = history.mainElement) {
                is Element.Image -> AsyncItemImage(mainItem.imageResource)
                is Element.Text -> Text(text = mainItem.text)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = remember(history.dateRange.format()) {
                    history.dateRange.format()
                },
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { onClickDelete(history.id) }) {
                Icon(Icons.Filled.Delete, "")
            }
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
            StoriesList(
                stories = Mocks().getMockStories(),
                onClickItem = {},
                deleteHistory = {},
            )
        }
    }
}

@Preview
@Composable
fun HistoryItem_preview() {
    StoriesTheme {
        HistoryItem(
            history = Mocks().getMockStories()[1],
            onClickItem = {},
            onClickDelete = {}
        )
    }
}

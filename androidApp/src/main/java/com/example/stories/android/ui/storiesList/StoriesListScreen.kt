package com.example.stories.android.ui.storiesList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    onClickItem: (Long) -> Unit,
) {
    val storiesLoadStatus by viewModel.storiesLoadStatus.collectAsStateWithLifecycle()
    LoadingDataScreen(storiesLoadStatus) { stories ->
        StoriesList(stories, onClickItem)
    }
}

@Composable
fun StoriesList(stories: List<History>, onClickItem: (Long) -> Unit) {
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

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(stories, key = { it.id }) {history ->
                    HistoryItem(history, onClickItem)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(history: History, onClickItem: (Long) -> Unit) {
    ItemCard(
        modifier = Modifier.clickable { onClickItem(history.id) }
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

        Text(
            text = remember(history.dateRange.format()) {
                history.dateRange.format()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )
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
                onClickItem = {}
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
            onClickItem = {}
        )
    }
}

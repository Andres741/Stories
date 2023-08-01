package com.example.stories.android.ui.historyDetail

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History

@Composable
fun HistoryDetailScreen(
    viewModel: HistoryDetailViewModel
) {
    val historyLoadStatus by viewModel.historyLoadStatus.collectAsStateWithLifecycle()
    LoadingDataScreen(loadStatus = historyLoadStatus) { history ->
        HistoryDetail(history)
    }
}

@Composable
fun HistoryDetail(
    history: History
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = history.title,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            history.mainImage?.let { mainImage ->
                AsyncImage(
                    model = mainImage,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16f.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.padding(top = 16f.dp)
            ) {
                items(history.elements) { historyElement ->
                    ElementItem(historyElement)
                }
            }
        }
    }
}

@Composable
fun ElementItem(historyElement: Element) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12f.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12f.dp)
    ) {
        when(historyElement) {
            is Element.Image -> ElementImageItem(historyElement)
            is Element.Text -> ElementTextItem(historyElement)
        }
    }
}

@Composable
fun ElementImageItem(image: Element.Image) {
    AsyncImage(
        model = image.imageResource,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
    )
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
            HistoryDetail(
                history = Mocks().getMockStories()[1],
            )
        }
    }
}

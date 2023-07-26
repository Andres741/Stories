package com.example.stories.android.storiesList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.SharedRes
import com.example.stories.android.MyApplicationTheme
import com.example.stories.android.R
import com.example.stories.android.resources.getStringResource
import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.History
import com.example.stories.infrastructure.date.formatNoteDate

@Composable
fun StoriesListScreen(
    viewModel: StoriesListViewModel,
    onClickItem: (Long) -> Unit,
) {
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    StoriesList(stories, onClickItem)
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
                text = getStringResource(resId = SharedRes.strings.stories_screen_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )

            LazyColumn {
                items(stories) {history ->
                    HistoryItem(history, onClickItem)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(history: History, onClickItem: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16f.dp)
            .padding(bottom = 12f.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(colorResource(R.color.card))
            .clickable { onClickItem(history.id) }
    ) {

        Text(
            text = history.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )

        history.mainImage?.let { mainImage ->
            AsyncImage(
                model = mainImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }

        Text(
            text = remember(history.date) {
                history.date.formatNoteDate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12f.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview
@Composable
fun StoriesList_preview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
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
    MyApplicationTheme {
        HistoryItem(
            history = Mocks().getMockStories()[1],
            onClickItem = {}
        )
    }
}

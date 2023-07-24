package com.example.stories.android.historyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stories.android.MyApplicationTheme
import com.example.stories.android.R
import com.example.stories.data.domain.mocks.Mocks
import com.example.stories.data.domain.model.Element
import com.example.stories.data.domain.model.History

@Composable
fun HistoryDetailScreen(
    viewModel: HistoryDetailViewModel
) {
    val history by viewModel.history.collectAsState()
    HistoryDetail(history ?: return)
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
                style = MaterialTheme.typography.h3
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
            .clip(RoundedCornerShape(5.dp))
            .padding(bottom = 12f.dp)
            .background(colorResource(R.color.card))
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
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.body1
    )
}


@Preview
@Composable
fun StoriesList_preview() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            HistoryDetail(
                history = Mocks().getMockStories()[1],
            )
        }
    }
}

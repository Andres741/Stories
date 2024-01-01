package com.example.stories.android.ui.communityHistoryDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.ElementsListBody
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks

@Composable
fun CommunityHistoryDetailScreen(
    viewModel: CommunityHistoryDetailViewModel,
) {
    val historyLoadStatus by viewModel.historyLoadStatus.collectAsStateWithLifecycle()

    LoadingDataScreen(loadStatus = historyLoadStatus) { history ->
        CommunityHistoryDetail(history)
    }
}

@Composable
fun CommunityHistoryDetail(
    history: History,
) {
    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Text(
                text = history.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
            )

            ElementsListBody(
                history = history,
                editMode = false,
                rotation = { 0f },
                onClickElement = {  },
                onClickDate = {  },
                swapElements = { _, _ ->  },
                deleteElement = {  },
            )
        }
    }
}

@Preview
@Composable
fun CommunityHistoryDetail_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CommunityHistoryDetail(
                history = HistoryMocks().getMockStories()[0]
            )
        }
    }
}

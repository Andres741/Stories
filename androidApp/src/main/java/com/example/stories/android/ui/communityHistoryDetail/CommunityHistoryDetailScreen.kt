package com.example.stories.android.ui.communityHistoryDetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.HISTORY_DATE_ITEM
import com.example.stories.android.ui.HISTORY_FIRST_ITEM
import com.example.stories.android.ui.HISTORY_TITLE
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.ElementsListBody
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.util.ui.RefreshLoadingDataScreen
import com.example.stories.android.util.ui.SharedTransitionStuff
import com.example.stories.android.util.ui.sharedTransition
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CommunityHistoryDetailScreen(
    viewModel: CommunityHistoryDetailViewModel,
    sharedTransitionStuff: SharedTransitionStuff,
) {
    val historyLoadStatus by viewModel.historyLoadStatus.collectAsStateWithLifecycle()

    RefreshLoadingDataScreen(loadStatus = historyLoadStatus, onRefresh = viewModel::refreshData) { history ->
        CommunityHistoryDetail(history, sharedTransitionStuff)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CommunityHistoryDetail(
    history: History,
    sharedTransitionStuff: SharedTransitionStuff,
) {
    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            TitleText(
                text = history.title,
                modifier = Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_TITLE/${history.id}"),
            )

            ElementsListBody(
                history = history,
                editMode = false,
                itemModifier = { index ->
                    if (index != 0) Modifier else Modifier.sharedTransition(
                        sharedTransitionStuff = sharedTransitionStuff,
                        key = "$HISTORY_FIRST_ITEM/${history.id}",
                    )
                },
                itemDateModifier = Modifier.sharedTransition(
                    sharedTransitionStuff = sharedTransitionStuff,
                    key = "$HISTORY_DATE_ITEM/${history.id}",
                ),
                rotation = { 0f },
                onClickElement = {  },
                onClickDate = {  },
                swapElements = { _, _ ->  },
                deleteElement = {  },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun CommunityHistoryDetail_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            SharedTransitionLayout transitionScope@{
                AnimatedContent(0) { state ->
                    println(state)
                    CommunityHistoryDetail(
                        history = HistoryMocks().getMockStories()[0],
                        sharedTransitionStuff = this@transitionScope to this@AnimatedContent,
                    )
                }
            }
        }
    }
}

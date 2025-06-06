package com.example.stories.android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.ui.AsyncItemImage
import com.example.stories.android.util.ui.EmptyScreen
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.infrastructure.date.format
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryElement
import com.example.stories.model.domain.model.HistoryMocks

@Composable
fun StoriesListBody(
    stories: List<History>,
    navigateDetail: (String) -> Unit,
    emptyScreenTitle: String,
    emptyScreenText: String,
    modifier: Modifier = Modifier,
    itemTextModifier: @Composable (History) -> Modifier = { Modifier },
    itemFirstModifier: @Composable (History) -> Modifier = { Modifier },
    itemDateModifier: @Composable (History) -> Modifier = { Modifier },
    listState: LazyListState = rememberLazyListState(),
    onClickDelete: ((String) -> Unit)? = null,
) {

    if (stories.isEmpty()) {
        EmptyScreen(
            title = emptyScreenTitle,
            text = emptyScreenText,
        )
        return
    }

    LazyColumn(modifier = modifier, state = listState) {
        items(stories, key = { it.id }) {history ->
            HistoryItem(
                history = history,
                onClickItem = navigateDetail,
                onClickDelete = onClickDelete,
                modifier = Modifier.animateItem(),
                itemTextModifier = itemTextModifier,
                itemFirstModifier = itemFirstModifier,
                itemDateModifier = itemDateModifier,
            )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun HistoryItem(
    history: History,
    onClickItem: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemTextModifier: @Composable (History) -> Modifier = { Modifier },
    itemFirstModifier: @Composable (History) -> Modifier = { Modifier },
    itemDateModifier: @Composable (History) -> Modifier = { Modifier },
    onClickDelete: ((String) -> Unit)? = null,
) {
    ItemCard(
        modifier = modifier.clickable { onClickItem(history.id) }
    ) {

        Text(
            text = history.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 6.dp)
                .then(itemTextModifier(history)),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )

        val itemModifier = itemFirstModifier(history)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 6.dp)
        ) {
            when (val mainItem = history.mainElement) {
                is HistoryElement.Image -> AsyncItemImage(mainItem.imageResource, itemModifier)
                is HistoryElement.Text -> Text(text = mainItem.text, itemModifier)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(
                text = remember(history.dateRange.format()) {
                    history.dateRange.format()
                },
                style = MaterialTheme.typography.labelLarge,
                modifier = itemDateModifier(history),
            )

            Spacer(modifier = Modifier.weight(1f))

            onClickDelete?.let {
                IconButton(onClick = { onClickDelete(history.id) }) {
                    Icon(Icons.Filled.Delete, "")
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryItem_preview() {
    StoriesTheme {
        HistoryItem(
            history = HistoryMocks().getMockStories()[1],
            onClickItem = {},
            onClickDelete = {}
        )
    }
}

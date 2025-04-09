package com.example.stories.android.ui.communityStories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.StoriesListBody
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.RefreshLoadingDataScreen
import com.example.stories.android.util.ui.collapsingToolbarLayout.CollapsingToolbarLayout
import com.example.stories.android.util.ui.collapsingToolbarLayout.lerp
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@Composable
fun CommunityStoriesListScreen(
    viewModel: CommunityStoriesListViewModel,
    navigateDetail: (String) -> Unit,
) {
    val storiesLoadStatus by viewModel.userAndStoriesLoadStatus.collectAsStateWithLifecycle()

    RefreshLoadingDataScreen(
        loadStatus = storiesLoadStatus,
        onRefresh = viewModel::refreshData,
        isDataEmpty = { (_, stories) -> stories.isEmpty() },
        refreshTitle = getStringResource { empty_stories_screen_title },
    ) { userAndStories ->
        val (user, stories) = userAndStories

        CommunityStoriesList(
            user = user,
            stories = stories,
            navigateDetail = navigateDetail,
        )
    }
}

private val MinToolbarHeight = 112.dp
private val MaxToolbarHeight = 248.dp

@Composable
fun CommunityStoriesList(
    user: User,
    stories: List<History>,
    navigateDetail: (String) -> Unit,
) {
    Scaffold { padding ->
        val userImage = user.profileImage
        if (userImage == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Header(user = user)
                StoriesListBody(
                    stories = stories,
                    navigateDetail = navigateDetail,
                    emptyScreenTitle = getStringResource { empty_history_list_title },
                    emptyScreenText = getStringResource { empty_community_history_list_text },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        } else {
            val density = LocalDensity.current
            val toolbarHeightRange = remember(LocalDensity.current) {
                density.run { MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx() }
            }

            CollapsingToolbarLayout(
                toolbarHeightRange = toolbarHeightRange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                toolbar = { toolbarModifier, toolbarState ->
                    CollapsingHeader(
                        userName = user.name,
                        userDescription = user.description,
                        userImage = userImage.url,
                        progress = toolbarState.progress,
                        modifier = toolbarModifier.padding(16.dp)
                    )
                },
                body = { bodyModifier, toolbarState, listState ->
                    StoriesListBody(
                        stories = stories,
                        navigateDetail = navigateDetail,
                        emptyScreenTitle = getStringResource { empty_history_list_title },
                        emptyScreenText = getStringResource { empty_community_history_list_text },
                        listState = listState,
                        modifier = bodyModifier.padding(horizontal = 16.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun Header(user: User) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = user.name,
            modifier = Modifier,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )
        if (user.description.isNotBlank()) {
            Text(user.description)
        }
    }
}

@Composable
private fun CollapsingHeader(
    userName: String,
    userDescription: String,
    userImage: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Layout(
            content = {
                AsyncImage(
                    model = userImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = userName,
                    modifier = Modifier.wrapContentSize(),
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(userDescription, modifier = Modifier.wrapContentSize())
            },
            measurePolicy = { measurable, constraints ->
                val placeables = measurable.map {
                    it.measure(constraints)
                }
                layout(
                    width = constraints.maxWidth,
                    height = constraints.maxHeight
                ) {
                    val horizontalGuideline = constraints.maxWidth / 2

                    val profileImage = placeables[0]
                    val name = placeables[1]
                    val description = placeables[2]

                    profileImage.placeRelativeWithLayer(
                        x = lerp(
                            start = 0,
                            stop = horizontalGuideline - profileImage.width / 2,
                            fraction = progress
                        ),
                        y = 0,
                    ) {
                        val scale = lerp(
                            start = 1f,
                            stop = 1.8f,
                            fraction = progress,
                        )
                        translationY = (profileImage.height * (scale - 1)) / 2
                        scaleY = scale
                        scaleX = scale
                    }

                    val nameY = lerp(
                        start = 0,
                        stop = 148.dp.roundToPx(),
                        fraction = progress,
                    )

                    name.placeRelative(
                        x = lerp(
                            start = 96.dp.roundToPx(),
                            stop = horizontalGuideline - name.width / 2,
                            fraction = progress,
                        ),
                        y = nameY,
                    )

                    description.placeRelative(
                        x = lerp(
                            start = 96.dp.roundToPx(),
                            stop = horizontalGuideline - description.width / 2,
                            fraction = progress,
                        ),
                        y = nameY + name.height,
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun CommunityStoriesList_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CommunityStoriesList(
                user = HistoryMocks().getMockUsers()[0],
                stories = HistoryMocks().getMockStories(),
                navigateDetail = {},
            )
        }
    }
}

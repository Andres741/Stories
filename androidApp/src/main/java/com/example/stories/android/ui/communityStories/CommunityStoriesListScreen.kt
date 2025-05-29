package com.example.stories.android.ui.communityStories

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import com.example.stories.android.ui.HISTORY_DATE_ITEM
import com.example.stories.android.ui.HISTORY_FIRST_ITEM
import com.example.stories.android.ui.HISTORY_TITLE
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.USER_DESCRIPTION
import com.example.stories.android.ui.USER_IMAGE
import com.example.stories.android.ui.USER_NAME
import com.example.stories.android.ui.components.StoriesListBody
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.RefreshLoadingDataScreen
import com.example.stories.android.util.ui.SharedTransitionStuff
import com.example.stories.android.util.ui.collapsingToolbarLayout.CollapsingToolbarLayout
import com.example.stories.android.util.ui.collapsingToolbarLayout.lerp
import com.example.stories.android.util.ui.sharedTransition
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CommunityStoriesListScreen(
    viewModel: CommunityStoriesListViewModel,
    navigateDetail: (String) -> Unit,
    sharedTransitionStuff: SharedTransitionStuff,
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
            sharedTransitionStuff = sharedTransitionStuff,
            navigateDetail = navigateDetail,
        )
    }
}

private val MinToolbarHeight = 112.dp
private val MaxToolbarHeight = 248.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CommunityStoriesList(
    user: User,
    stories: List<History>,
    sharedTransitionStuff: SharedTransitionStuff,
    navigateDetail: (String) -> Unit,
) {
    Scaffold { padding ->
        val userImage = user.profileImage

        val itemTextModifier = @Composable { history: History ->
            Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_TITLE/${history.id}")
        }

        val itemFirstModifier = @Composable { history: History ->
            Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_FIRST_ITEM/${history.id}")
        }

        val itemDateModifier = @Composable { history: History ->
            Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_DATE_ITEM/${history.id}")
        }

        if (userImage == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Header(user = user, sharedTransitionStuff = sharedTransitionStuff)
                StoriesListBody(
                    stories = stories,
                    navigateDetail = navigateDetail,
                    emptyScreenTitle = getStringResource { empty_history_list_title },
                    emptyScreenText = getStringResource { empty_community_history_list_text },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    itemTextModifier = itemTextModifier,
                    itemFirstModifier = itemFirstModifier,
                    itemDateModifier = itemDateModifier,
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
                        user = user,
                        userImage = userImage.url,
                        progress = toolbarState.progress,
                        modifier = toolbarModifier.padding(16.dp),
                        sharedTransitionStuff = sharedTransitionStuff,
                    )
                },
                body = { bodyModifier, _, listState ->
                    StoriesListBody(
                        stories = stories,
                        navigateDetail = navigateDetail,
                        emptyScreenTitle = getStringResource { empty_history_list_title },
                        emptyScreenText = getStringResource { empty_community_history_list_text },
                        listState = listState,
                        modifier = bodyModifier.padding(horizontal = 16.dp),
                        itemTextModifier = itemTextModifier,
                        itemFirstModifier = itemFirstModifier,
                        itemDateModifier = itemDateModifier,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Header(user: User, sharedTransitionStuff: SharedTransitionStuff) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(
            text = user.name,
            modifier = Modifier.sharedTransition(sharedTransitionStuff, "$USER_NAME/${user.id}"),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
        )
        if (user.description.isNotBlank()) {
            Text(
                text = user.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.sharedTransition(sharedTransitionStuff, "$USER_DESCRIPTION/${user.id}"),
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CollapsingHeader(
    user: User,
    userImage: String,
    progress: Float,
    modifier: Modifier = Modifier,
    sharedTransitionStuff: SharedTransitionStuff,
) {
    Box(modifier = modifier) {
        Layout(
            content = {
                val scale = lerp(
                    start = 1f,
                    stop = 2f,
                    fraction = progress,
                )

                AsyncImage(
                    model = userImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .sharedTransition(sharedTransitionStuff, "$USER_IMAGE/${user.id}")
                        .size((70 * scale).dp)
                        .clip(CircleShape),
                )
                Text(
                    text = user.name,
                    modifier = Modifier
                        .wrapContentSize()
                        .sharedTransition(sharedTransitionStuff, "$USER_NAME/${user.id}"),
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(
                    text = user.description,
                    modifier = Modifier
                        .wrapContentSize()
                        .sharedTransition(sharedTransitionStuff, "$USER_DESCRIPTION/${user.id}"),
                )
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
                        y = lerp(
                            start = 12.dp.roundToPx(),
                            stop = 0,
                            fraction = progress,
                        ),
                    )

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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun CommunityStoriesList_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            SharedTransitionLayout transitionScope@{
                AnimatedContent(0) { state ->
                    println(state)
                    CommunityStoriesList(
                        user = HistoryMocks().getMockUsers()[0],
                        stories = HistoryMocks().getMockStories(),
                        sharedTransitionStuff = this@transitionScope to this@AnimatedContent,
                        navigateDetail = {},
                    )
                }
            }
        }
    }
}

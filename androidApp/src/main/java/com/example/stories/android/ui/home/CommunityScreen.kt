package com.example.stories.android.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.USER_DESCRIPTION
import com.example.stories.android.ui.USER_IMAGE
import com.example.stories.android.ui.USER_NAME
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.android.util.ui.RefreshLoadingDataScreen
import com.example.stories.android.util.ui.SharedTransitionStuff
import com.example.stories.android.util.ui.sharedTransition
import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel,
    navigateToStories: (userId: String?) -> Unit,
    sharedTransitionStuff: SharedTransitionStuff,
) {

    val usersLoadStatus by viewModel.users.collectAsStateWithLifecycle()

    Community(
        usersLoadStatus = usersLoadStatus,
        onRefresh = viewModel::refreshData,
        navigateToStories = navigateToStories,
        sharedTransitionStuff = sharedTransitionStuff,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Community(
    usersLoadStatus: LoadStatus<List<User>>,
    onRefresh: (showLoading: Boolean) -> Unit,
    navigateToStories: (userId: String?) -> Unit,
    sharedTransitionStuff: SharedTransitionStuff,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToStories(null) }) {
                Icon(Icons.Filled.AccountCircle, "")
            }
        }
    ) { padding ->
        RefreshLoadingDataScreen(
            loadStatus = usersLoadStatus,
            onRefresh = onRefresh,
            isDataEmpty = { users -> users.isEmpty() },
            refreshTitle = getStringResource { empty_users_screen_title },
        ) { users ->
            Column(modifier = Modifier
                .padding(padding)
                .fillMaxSize()) {
                TitleText(
                    text = getStringResource { community },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(users, key = { it.id }) { user ->
                        UserItem(
                            user = user,
                            modifier = Modifier.clickable { navigateToStories(user.id) },
                            sharedTransitionStuff = sharedTransitionStuff,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserItem(
    user: User,
    modifier: Modifier = Modifier,
    sharedTransitionStuff: SharedTransitionStuff,
) {
    ItemCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            user.profileImage?.let { profileImage ->
                AsyncImage(
                    model = profileImage.url,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(70.dp)
                        .sharedTransition(sharedTransitionStuff, "$USER_IMAGE/${user.id}")
                        .clip(CircleShape)
                )
            }
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.sharedTransition(sharedTransitionStuff, "$USER_NAME/${user.id}"),
                )
                Text(
                    text = user.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.sharedTransition(sharedTransitionStuff, "$USER_DESCRIPTION/${user.id}"),
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun CommunityScreen_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout transitionScope@{
                AnimatedContent(0) { state ->
                    println(state)
                    Community(
                        usersLoadStatus = LoadStatus.Data(HistoryMocks().getMockUsers()),
                        onRefresh = {},
                        navigateToStories = {},
                        sharedTransitionStuff = this@transitionScope to this@AnimatedContent,
                    )
                }
            }
        }
    }
}

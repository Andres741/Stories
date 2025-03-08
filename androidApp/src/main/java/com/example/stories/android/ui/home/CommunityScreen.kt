package com.example.stories.android.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.ItemCard
import com.example.stories.android.util.ui.RefreshLoadingDataScreen
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel,
    navigateToStories: (userId: String?) -> Unit,
    navigateTest: () -> Unit,
) {

    val usersLoadStatus by viewModel.users.collectAsStateWithLifecycle()

    RefreshLoadingDataScreen(loadStatus = usersLoadStatus, onRefresh = viewModel::refreshData) { users ->
        Community(
            users = users,
            navigateToStories = navigateToStories,
            navigateTest = navigateTest,
        )
    }
}

@Composable
fun Community(
    users: List<User>,
    navigateToStories: (userId: String?) -> Unit,
    navigateTest: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToStories(null) }) {
                Icon(Icons.Filled.AccountCircle, "")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column {
                TitleText(
                    text = getStringResource { community },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    items(users, key = { it.id }) { user ->
                        UserItem(
                            user = user,
                            modifier = Modifier.clickable { navigateToStories(user.id) }
                        )
                    }
                }
            }

            Button(
                onClick = { navigateTest() },
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            ) {
                Text(text = "Test")
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    modifier: Modifier = Modifier,
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
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = user.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun CommunityScreen_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Community(
                users = HistoryMocks().getMockUsers(),
                navigateToStories = {},
                navigateTest = {},
            )
        }
    }
}

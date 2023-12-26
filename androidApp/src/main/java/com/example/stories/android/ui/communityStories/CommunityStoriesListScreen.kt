package com.example.stories.android.ui.communityStories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.StoriesColumn
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@Composable
fun CommunityStoriesListScreen(
    viewModel: CommunityStoriesListViewModel,
    navigateDetail: (String) -> Unit,
) {
    val storiesLoadStatus by viewModel.userAndStories.collectAsStateWithLifecycle()

    LoadingDataScreen(loadStatus = storiesLoadStatus) { userAndStories ->
        val (user, stories) = userAndStories

        CommunityStoriesList(
            user = user,
            stories = stories,
            navigateDetail = navigateDetail,
        )
    }
}

@Composable
fun CommunityStoriesList(
    user: User,
    stories: List<History>,
    navigateDetail: (String) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Header(user = user)
            StoriesColumn(
                stories = stories,
                navigateDetail = navigateDetail,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun Header(user: User) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        user.profileImage?.let { profileImage ->
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }
        Column {
            Text(
                text = user.name,
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            Text(text = user.description)
        }
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

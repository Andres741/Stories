package com.example.stories.android.ui.storiesList

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.HISTORY_DATE_ITEM
import com.example.stories.android.ui.HISTORY_FIRST_ITEM
import com.example.stories.android.ui.HISTORY_TITLE
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.Banner
import com.example.stories.android.ui.components.StoriesListBody
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.android.util.ui.SharedTransitionStuff
import com.example.stories.android.util.ui.sharedTransition
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.HistoryMocks
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StoriesListScreen(
    viewModel: StoriesListViewModel,
    sharedTransitionStuff: SharedTransitionStuff,
    navigateDetail: (String) -> Unit,
    navigateLogIn: () -> Unit,
    navigateUserData: () -> Unit,
) {
    val storiesLoadStatus by viewModel.storiesLoadStatus.collectAsStateWithLifecycle()
    LoadingDataScreen(storiesLoadStatus) { stories ->
        val navigateHistory by viewModel.newHistory.collectAsStateWithLifecycle()
        val isLogged by viewModel.isLogged.collectAsStateWithLifecycle()

        StoriesList(
            stories = stories,
            navigateHistory = navigateHistory,
            isLogged = isLogged,
            sharedTransitionStuff = sharedTransitionStuff,
            navigateDetail = navigateDetail,
            navigateLogIn = navigateLogIn,
            navigateUserData = navigateUserData,
            deleteHistory = viewModel::deleteHistory,
            createBasicHistory = viewModel::createBasicHistory,
            onNewHistoryConsumed = viewModel::onNewHistoryConsumed,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StoriesList(
    stories: List<History>,
    navigateHistory: History?,
    isLogged: Boolean?,
    sharedTransitionStuff: SharedTransitionStuff,
    navigateDetail: (String) -> Unit,
    navigateLogIn: () -> Unit,
    navigateUserData: () -> Unit,
    deleteHistory: (String) -> Unit,
    createBasicHistory: (title: String, text: String) -> Unit,
    onNewHistoryConsumed: () -> Unit,
) {
    val basicHistoryTitle = getStringResource { basic_history_title }
    val basicHistoryText = getStringResource { basic_history_text }

    LaunchedEffect(key1 = navigateHistory) {
        navigateHistory?.id?.let { newId ->
            navigateDetail(newId)
            onNewHistoryConsumed()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createBasicHistory(basicHistoryTitle, basicHistoryText)
                }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TitleText(
                text = getStringResource { stories_screen_title },
                modifier = Modifier.padding(bottom = 12.dp),
            )

            LoggingBanner(
                isLogged = isLogged,
                navigateLogIn = navigateLogIn,
                navigateUserData = navigateUserData,
            )

            var deletingHistoryId by remember { mutableStateOf(null as String?) }
            deletingHistoryId?.let { id ->
                AlertDialog(
                    onDismissRequest = { deletingHistoryId = null },
                    title = { Text(getStringResource { delete_history_pop_up_title }) },
                    text = { Text(getStringResource { delete_history_pop_up_text }) },
                    confirmButton = {
                        TextButton(onClick = { deleteHistory(id); deletingHistoryId = null }) {
                            Text(getStringResource { accept })
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { deletingHistoryId = null }) {
                            Text(getStringResource { dismiss })
                        }
                    },
                )
            }

            StoriesListBody(
                stories = stories,
                navigateDetail = navigateDetail,
                emptyScreenTitle = getStringResource { empty_history_list_title },
                emptyScreenText = getStringResource { empty_history_list_text },
                itemTextModifier = { history ->
                    Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_TITLE/${history.id}")
                },
                itemFirstModifier = { history ->
                    Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_FIRST_ITEM/${history.id}")
                },
                itemDateModifier = { history ->
                    Modifier.sharedTransition(sharedTransitionStuff, "$HISTORY_DATE_ITEM/${history.id}")
                },
                onClickDelete = { deletingHistoryId = it },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun LoggingBanner(
    isLogged: Boolean?,
    navigateLogIn: () -> Unit,
    navigateUserData: () -> Unit,
) {
    AnimatedVisibility(
        visible = isLogged != null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Banner(
            bannerText = getStringResource {
                if (isLogged == true) logged_warn
                else not_logged_warn
            },
            onClickBanner = if (isLogged == true) navigateUserData else navigateLogIn,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun StoriesList_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var showLogged by remember { mutableStateOf(true) }

            LaunchedEffect(key1 = showLogged, block = {
                if (!showLogged) {
                    delay(1000)
                    showLogged = true
                }
            })

            SharedTransitionLayout transitionScope@{
                AnimatedContent(0) { state ->
                    println(state)

                    StoriesList(
                        stories = HistoryMocks().getMockStories(),
                        navigateHistory = null,
                        isLogged = showLogged.not(),
                        sharedTransitionStuff = this@transitionScope to this@AnimatedContent,
                        navigateDetail = {},
                        navigateLogIn = { showLogged = false },
                        navigateUserData = {},
                        deleteHistory = {},
                        createBasicHistory = { _, _ -> },
                        onNewHistoryConsumed = {},
                    )
                }
            }
        }
    }
}

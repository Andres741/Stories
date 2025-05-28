package com.example.stories.android.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.stories.android.ui.communityHistoryDetail.CommunityHistoryDetailScreen
import com.example.stories.android.ui.communityHistoryDetail.CommunityHistoryDetailViewModel
import com.example.stories.android.ui.communityStories.CommunityStoriesListScreen
import com.example.stories.android.ui.communityStories.CommunityStoriesListViewModel
import com.example.stories.android.ui.editUserData.EditUserDataScreen
import com.example.stories.android.ui.historyDetail.HistoryDetailScreen
import com.example.stories.android.ui.historyDetail.HistoryDetailViewModel
import com.example.stories.android.ui.home.CommunityScreen
import com.example.stories.android.ui.logIn.LogInScreen
import com.example.stories.android.ui.storiesList.StoriesListScreen
import com.example.stories.android.ui.userData.UserDataScreen
import com.example.stories.android.util.ui.SharedTransitionStuff
import com.example.stories.android.util.ui.sharedTransition
import com.example.stories.android.util.ui.test.TestImagePickerScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContent() {
    StoriesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            SharedTransitionLayout transitionScope@{
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.Community) {
                    composable<Routes.Community> {
                        CommunityScreen(
                            viewModel = viewModel(),
                            navigateToStories = { userId ->
                                if (userId == null) navController.navigate(Routes.Stories)
                                else navController.navigate(Routes.CommunityStories(userId))
                            },
                        )
                    }
                    composable<Routes.Stories> animatedContentScope@{
                        StoriesListScreen(
                            viewModel = viewModel(),
                            sharedTransitionStuff = this@transitionScope to this@animatedContentScope,
                            navigateDetail = { navController.navigate(Routes.HistoryDetail(it)) },
                            navigateLogIn = { navController.navigate(Routes.Login) },
                            navigateUserData = { navController.navigate(Routes.UserData) }
                        )
                    }
                    composable<Routes.HistoryDetail> animatedContentScope@{ backStackEntry ->
                        val historyDetailRoute = backStackEntry.toRoute<Routes.HistoryDetail>()
                        HistoryDetailScreen(
                            viewModel = viewModel(factory = HistoryDetailViewModel.Factory(historyDetailRoute.historyId)),
                            sharedTransitionStuff = this@transitionScope to this@animatedContentScope,
                        )
                    }
                    composable<Routes.CommunityStories> { backStackEntry ->
                        val communityRoute = backStackEntry.toRoute<Routes.CommunityStories>()

                        CommunityStoriesListScreen(
                            viewModel = viewModel(factory = CommunityStoriesListViewModel.Factory(communityRoute.userId)),
                            navigateDetail = { historyId ->
                                navController.navigate(Routes.CommunityHistoryDetail(
                                    userId = communityRoute.userId,
                                    historyId = historyId,
                                ))
                            }
                        )
                    }
                    composable<Routes.CommunityHistoryDetail> { backStackEntry ->
                        val communityHistoryDetailRoute = backStackEntry.toRoute<Routes.CommunityHistoryDetail>()
                        CommunityHistoryDetailScreen(
                            viewModel = viewModel(
                                factory = CommunityHistoryDetailViewModel.Factory(
                                    historyId = communityHistoryDetailRoute.historyId,
                                    userId = communityHistoryDetailRoute.userId,
                                )
                            )
                        )
                    }
                    composable<Routes.Login> {
                        LogInScreen(
                            viewModel = viewModel(),
                            navigateBack = navController::popBackStack,
                        )
                    }
                    composable<Routes.UserData> {
                        UserDataScreen(
                            viewModel = viewModel(),
                            navigateEditUser = { navController.navigate(Routes.EditUserData) },
                        )
                    }
                    composable<Routes.EditUserData> {
                        EditUserDataScreen(
                            viewModel = viewModel(),
                            navigateBack = navController::popBackStack,
                        )
                    }
                    composable<Routes.TestImages> {
                        TestImagePickerScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.historyTitleIdSharedTransition(
    sharedTransitionStuff: SharedTransitionStuff,
    historyId: String,
) : Modifier {
    return sharedTransition(sharedTransitionStuff, "HISTORY_TITLE/$historyId")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.historyFirstItemIdSharedTransition(
    sharedTransitionStuff: SharedTransitionStuff,
    historyId: String,
) : Modifier {
    return sharedTransition(sharedTransitionStuff, "HISTORY_FIRST_ITEM/$historyId")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.historyDateItemIdSharedTransition(
    sharedTransitionStuff: SharedTransitionStuff,
    historyId: String,
) : Modifier {
    return sharedTransition(sharedTransitionStuff, "HISTORY_DATE_ITEM/$historyId")
}

sealed interface Routes {
    @Serializable
    data object Community : Routes
    @Serializable
    data object Stories : Routes
    @Serializable
    data class CommunityStories(val userId: String) : Routes
    @Serializable
    data class HistoryDetail(val historyId: String) : Routes
    @Serializable
    data class CommunityHistoryDetail(val userId: String, val historyId: String) : Routes
    @Serializable
    data object Login : Routes
    @Serializable
    data object UserData : Routes
    @Serializable
    data object EditUserData : Routes
    @Serializable
    data object TestImages : Routes
}

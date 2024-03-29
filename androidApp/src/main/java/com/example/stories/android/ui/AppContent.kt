package com.example.stories.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

@Composable
fun AppContent() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.COMMUNITY.toString()) {
                composable(route = Routes.COMMUNITY.getRoute()) {
                    CommunityScreen(
                        viewModel = viewModel(),
                        navigateToStories = { userId ->
                            if (userId == null) navController.navigate(Routes.STORIES.name)
                            else navController.navigate(Routes.COMMUNITY_STORIES.getDestinationRoute(userId))
                        },
                    )
                }
                composable(route = Routes.STORIES.getRoute()) {
                    StoriesListScreen(
                        viewModel = viewModel(),
                        navigateDetail = { navController.navigate(Routes.HISTORY_DETAIL.getDestinationRoute(it)) },
                        navigateLogIn = { navController.navigate(Routes.LOGIN.name) },
                        navigateUserData = { navController.navigate(Routes.USER_DATA.name) }
                    )
                }
                composable(
                    route = Routes.COMMUNITY_STORIES.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.COMMUNITY_STORIES.params[0]) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                    ),
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString(Routes.COMMUNITY_STORIES.params[0]) ?: ""

                    CommunityStoriesListScreen(
                        viewModel = viewModel(factory = CommunityStoriesListViewModel.Factory(userId)),
                        navigateDetail = { historyId ->
                            navController.navigate(Routes.COMMUNITY_HISTORY_DETAIL.getDestinationRoute(historyId, userId))
                        }
                    )
                }
                composable(
                    route = Routes.HISTORY_DETAIL.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.HISTORY_DETAIL.params[0]) {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                    ),
                ) { backStackEntry ->
                    val historyId = backStackEntry.arguments?.getString(Routes.HISTORY_DETAIL.params[0]) ?: ""
                    HistoryDetailScreen(
                        viewModel = viewModel(factory = HistoryDetailViewModel.Factory(historyId))
                    )
                }
                composable(
                    route = Routes.COMMUNITY_HISTORY_DETAIL.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.COMMUNITY_HISTORY_DETAIL.params[0]) {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                    ),
                ) { backStackEntry ->
                    val historyId = backStackEntry.arguments?.getString(Routes.COMMUNITY_HISTORY_DETAIL.params[0]) ?: ""
                    val userId = backStackEntry.arguments?.getString(Routes.COMMUNITY_HISTORY_DETAIL.params[1]) ?: ""
                    CommunityHistoryDetailScreen(
                        viewModel = viewModel(
                            factory = CommunityHistoryDetailViewModel.Factory(
                                historyId = historyId,
                                userId = userId,
                            )
                        )
                    )
                }
                composable(route = Routes.LOGIN.getRoute()) {
                    LogInScreen(
                        viewModel = viewModel(),
                        navigateBack = navController::popBackStack,
                    )
                }
                composable(route = Routes.USER_DATA.getRoute()) {
                    UserDataScreen(
                        viewModel = viewModel(),
                        navigateEditUser = { navController.navigate(Routes.EDIT_USER_DATA.name) },
                    )
                }
                composable(route = Routes.EDIT_USER_DATA.getRoute()) {
                    EditUserDataScreen(
                        viewModel = viewModel(),
                        navigateBack = navController::popBackStack,
                    )
                }
            }
        }
    }
}

enum class Routes(val params: Array<String> = emptyArray()) {
    COMMUNITY,
    STORIES,
    COMMUNITY_STORIES(arrayOf("userId")),
    HISTORY_DETAIL(arrayOf("historyId")),
    COMMUNITY_HISTORY_DETAIL(arrayOf("historyId", "userId")),
    LOGIN,
    USER_DATA,
    EDIT_USER_DATA,
    ;

    fun getDestinationRoute(vararg args: String?) = "$this${buildString { args.forEach { append("/$it") } }}"
    fun getRoute() = "$this${if (params.isEmpty()) "" else buildString { params.forEach { append("/{$it}") } }}"
}

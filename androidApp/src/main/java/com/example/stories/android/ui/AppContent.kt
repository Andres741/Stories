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
import com.example.stories.android.ui.communityStories.CommunityStoriesListScreen
import com.example.stories.android.ui.communityStories.CommunityStoriesListViewModel
import com.example.stories.android.ui.historyDetail.HistoryDetailScreen
import com.example.stories.android.ui.historyDetail.HistoryDetailViewModel
import com.example.stories.android.ui.home.HomeScreen
import com.example.stories.android.ui.storiesList.StoriesListScreen

@Composable
fun AppContent() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.HOME.toString()) {
                composable(route = Routes.HOME.getRoute()) {
                    HomeScreen(
                        homeViewModel = viewModel(),
                        navigateToStories = {
                            navController.navigate(Routes.STORIES.getDestinationRoute(it))
                        },
                    )
                }
                composable(
                    route = Routes.STORIES.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.STORIES.params!!) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                    ),
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString(Routes.STORIES.params)

                    if (userId == null) {
                        StoriesListScreen(
                            viewModel = viewModel(),
                            navigateDetail = {
                                navController.navigate(Routes.HISTORY_DETAIL.getDestinationRoute(it))
                            },
                        )
                    } else {
                        CommunityStoriesListScreen(
                            viewModel = viewModel(factory = CommunityStoriesListViewModel.Factory(userId)),
                            navigateDetail = { /* TODO */ }
                        )
                    }
                }
                composable(
                    route = Routes.HISTORY_DETAIL.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.HISTORY_DETAIL.params!!) {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                    ),
                ) { backStackEntry ->
                    val historyId = backStackEntry.arguments?.getString(Routes.HISTORY_DETAIL.params) ?: ""
                    HistoryDetailScreen(
                        viewModel = viewModel(factory = HistoryDetailViewModel.Factory(historyId))
                    )
                }
            }
        }
    }
}

enum class Routes(val params: String?) {
    HOME(null), STORIES("userId"), HISTORY_DETAIL("historyId");

    fun getDestinationRoute(arg: Any?) = "$this/$arg"
    fun getRoute() = "$this${if (params == null) "" else "/{$params}"}"
}

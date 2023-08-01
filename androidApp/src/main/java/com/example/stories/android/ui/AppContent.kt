package com.example.stories.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stories.android.ui.historyDetail.HistoryDetailScreen
import com.example.stories.android.ui.historyDetail.HistoryDetailViewModel
import com.example.stories.android.ui.storiesList.StoriesListScreen
import com.example.stories.android.ui.storiesList.StoriesListViewModel

@Composable
fun AppContent() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.STORIES.toString()) {
                composable(route = Routes.STORIES.toString()) {
                    StoriesListScreen(
                        viewModel = remember { StoriesListViewModel() },
                        onClickItem = {
                            navController.navigate(Routes.HISTORY_DETAIL.getDestinationRoute("${it}L"))
                        }
                    )
                }
                composable(
                    route = Routes.HISTORY_DETAIL.getRoute(),
                    arguments = listOf(
                        navArgument(name = Routes.HISTORY_DETAIL.params!!) {
                            type = NavType.LongType
                            defaultValue = -1L
                        }
                    )
                ) { backStackEntry ->
                    val historyId = backStackEntry.arguments?.getLong(Routes.HISTORY_DETAIL.params) ?: -1L
                    HistoryDetailScreen(
                        viewModel = remember(historyId) { HistoryDetailViewModel(historyId) }
                    )
                }
            }
        }
    }
}

enum class Routes(val params: String?) {
    STORIES(null), HISTORY_DETAIL("historyId");

    fun getDestinationRoute(arg: Any?) = "$this/$arg"
    fun getRoute() = "$this${if (params == null) "" else "/{$params}"}"
}

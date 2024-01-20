package com.example.stories.android.util.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.stories.android.util.ui.material2Recovered.swipeRefresh.PullRefreshIndicator
import com.example.stories.android.util.ui.material2Recovered.swipeRefresh.pullRefresh
import com.example.stories.android.util.ui.material2Recovered.swipeRefresh.rememberPullRefreshState

@Composable
fun PullRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(
        modifier = modifier.pullRefresh(pullRefreshState),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        content()
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

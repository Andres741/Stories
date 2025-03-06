package com.example.stories.android.util.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.resources.getString
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.resources.getPainterResource
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.infrastructure.loading.LoadStatus

@Composable
fun<T: Any> RefreshLoadingDataScreen(
    loadStatus: LoadStatus<T>,
    onRefresh: () -> Unit,
    errorContent: @Composable (LoadingError) -> Unit = {
        DefaultErrorScreen(
            loadingError = it,
            buttonText = getStringResource { refresh },
            onClickButton = onRefresh,
        )
    },
    loadingContent: @Composable () -> Unit = { DefaultLoadingScreen() },
    successContent: @Composable BoxScope.(T) -> Unit
) {
    loadStatus.fold(
        onError = { errorContent(it) },
        onLoading = { loadingContent() },
        onSuccess = {
            PullRefreshLayout(
                isRefreshing = loadStatus.isRefreshing(),
                onRefresh = onRefresh,
            ) {
                successContent(it)
            }
        },
    )
}

@Composable
fun<T: Any> LoadingDataScreen(
    loadStatus: LoadStatus<T>,
    errorContent: @Composable (LoadingError) -> Unit = { DefaultErrorScreen(it) },
    loadingContent: @Composable () -> Unit = { DefaultLoadingScreen() },
    successContent: @Composable (T) -> Unit
) {
    loadStatus.fold(
        onError = { errorContent(it) },
        onLoading = { loadingContent() },
        onSuccess = { successContent(it) },
    )
}

@Composable
fun DefaultErrorScreen(
    loadingError: LoadingError,
    buttonText: String = getStringResource { accept },
    onClickButton: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24f.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = getPainterResource(imageResource = loadingError.icon),
                modifier = Modifier
                    .padding(16.dp)
                    .size(36f.dp),
                contentDescription = "",
            )
            Text(
                text = loadingError.title.getString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = loadingError.message?.getString() ?: return@Column,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 30f.dp)
            )
        }
        Button(
            onClick = onClickButton ?: return,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(text = buttonText)
        }
    }
}

@Preview
@Composable
fun DefaultErrorScreen_preview() {
    StoriesTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val context = LocalContext.current
            val toast = remember {
                Toast.makeText(
                    context,
                    "Can do nothing bro, this is just a preview",
                    Toast.LENGTH_LONG
                )
            }
            DefaultErrorScreen(
                loadingError = LoadingError.GenericError,
                onClickButton = toast::show
            )
        }
    }
}

@Composable
fun DefaultLoadingScreen() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val dimen = min(maxHeight, maxWidth) / 3
        CircularProgressIndicator(
            modifier = Modifier
                .size(dimen)
                .align(Alignment.Center),
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview
@Composable
fun DefaultLoadingScreen_preview() {
    StoriesTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DefaultLoadingScreen()
        }
    }
}

package com.example.stories.android.util.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.stories.SharedRes
import com.example.stories.android.MyApplicationTheme
import com.example.stories.android.util.resources.getColorResource
import com.example.stories.android.util.resources.getString
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.resources.sharedPainterResource
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.infrastructure.loading.LoadStatus

@Composable
fun<T: Any> LoadingDataScreen(
    loadStatus: LoadStatus<T>,
    errorContent: @Composable (LoadingError) -> Unit = { DefaultErrorScreen(it) },
    loadingContent: @Composable () -> Unit = { DefaultLoadingScreen() },
    successContent: @Composable (T) -> Unit
) {
    when (loadStatus) {
        is LoadStatus.Error -> errorContent(loadStatus.error)
        LoadStatus.Loading -> loadingContent()
        is LoadStatus.Data -> successContent(loadStatus.value)
    }
}

@Composable
fun DefaultErrorScreen(
    loadingError: LoadingError,
    onClickButton: (() -> Unit)? = null
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
                painter = sharedPainterResource(imageResource = loadingError.icon),
                modifier = Modifier
                    .padding(16.dp)
                    .size(36f.dp),
                contentDescription = "",
            )
            Text(
                text = loadingError.title.getString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
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
                backgroundColor = getColorResource { error },
                contentColor = Color.Black
            )
        ) {
            Text(text = getStringResource(SharedRes.strings.accept))
        }
    }
}

@Preview
@Composable
fun DefaultErrorScreen_preview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            val context = LocalContext.current
            DefaultErrorScreen(
                loadingError = LoadingError.GenericError,
                onClickButton = {
                    Toast.makeText(
                        context,
                        "Can do nothing bro, this is just a preview",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            DefaultLoadingScreen()
        }
    }
}

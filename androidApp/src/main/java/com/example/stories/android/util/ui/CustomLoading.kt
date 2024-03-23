package com.example.stories.android.util.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.StoriesTheme

@Composable
fun CustomLoading(modifier: Modifier = Modifier) {

    val loadingColor = MaterialTheme.colorScheme.primary

    val transition = rememberInfiniteTransition(label = "",)

    val rotationProvider = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "",
    ).run { { value } }

    val angleProvider = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "",
    ).run { { value } }

    Canvas(modifier = modifier.aspectRatio(1f)) {
        drawArc(
            color = loadingColor,
            startAngle = rotationProvider(),
            sweepAngle = angleProvider(),
            useCenter = false,
            style = Stroke(width = 5.dp.toPx()),
        )
    }
}

@Preview
@Composable
fun CustomLoading_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                CustomLoading(modifier = Modifier.align(Alignment.Center).width(150.dp))
            }
        }
    }
}

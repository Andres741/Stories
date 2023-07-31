package com.example.stories.android.util.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.stories.SharedRes
import dev.icerock.moko.resources.ImageResource

@Composable
fun sharedPainterResource(imageResource: ImageResource): Painter {
    return painterResource(id = imageResource.drawableResId)
}

@Composable
inline fun sharedPainterResource(resource: SharedRes.images.() -> ImageResource): Painter {
    return sharedPainterResource(imageResource = SharedRes.images.resource())
}

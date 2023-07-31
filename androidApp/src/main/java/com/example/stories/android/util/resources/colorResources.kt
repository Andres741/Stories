package com.example.stories.android.util.resources

import android.content.Context
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.stories.SharedRes
import dev.icerock.moko.resources.ColorResource

@ColorInt
inline fun getColorResource(context: Context, resource: SharedRes.colors.() -> ColorResource): Int {
    return SharedRes.colors.resource().getColor(context)
}

@Composable
@ColorInt
fun getColorResource(colorResource: ColorResource): Color {
    val context = LocalContext.current
    return Color(colorResource.getColor(context))
}

@Composable
@ColorInt
inline fun getColorResource(resource: SharedRes.colors.() -> ColorResource): Color {
    return getColorResource(colorResource = SharedRes.colors.resource())
}

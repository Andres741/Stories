package com.example.stories.android.util.resources

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.stories.SharedRes
import com.example.stories.Strings
import com.example.stories.infrastructure.StringContainer
import com.example.stories.infrastructure.StringResourceContainer
import com.example.stories.infrastructure.TextContainer
import dev.icerock.moko.resources.StringResource

fun getStringResource(context: Context, resId: StringResource, vararg args: Any): String {
    return Strings(context).get(resId, args.asList())
}

inline fun getStringResource(context: Context, resources: SharedRes.strings.() -> StringResource): String {
    return SharedRes.strings.resources().getString(context)
}

@Composable
fun getStringResource(resId: StringResource): String {
    val context = LocalContext.current
    return remember(resId) {
        resId.getString(context)
    }
}

@Composable
inline fun getStringResource(resource: SharedRes.strings.() -> StringResource): String {
    return getStringResource(resId = SharedRes.strings.resource())
}

@Composable
fun getStringResource(resId: StringResource, vararg args: Any): String {
    val context = LocalContext.current
    val string = remember(resId) {
        getStringResource(context, resId, args)
    }
    return string
}

fun TextContainer.getString(context: Context): String = when(this) {
    is StringContainer -> text
    is StringResourceContainer -> resId.getString(context)
}

@Composable
fun TextContainer.getString(): String = when(this) {
    is StringContainer -> text
    is StringResourceContainer -> getStringResource(resId)
}

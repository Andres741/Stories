package com.example.stories.android.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.stories.Strings
import dev.icerock.moko.resources.StringResource

@Composable
fun getStringResource(resId: StringResource): String {
    return Strings(LocalContext.current).get(resId)
}

@Composable
fun getStringResource(resId: StringResource, vararg args: Any): String {
    return Strings(LocalContext.current).get(resId, args.asList())
}

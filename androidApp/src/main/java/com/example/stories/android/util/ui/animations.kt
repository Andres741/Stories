package com.example.stories.android.util.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

@Composable
fun actionableFloatAnimation(
    isActive: Boolean,
    disabledValue: Float,
    values: List<Float>,
    animationSpec: AnimationSpec<Float> = tween()
): State<Float> {
    val animation = remember { Animatable(disabledValue) }

    LaunchedEffect(key1 = isActive) {
        while (isActive) values.forEach {
            animation.animateTo(it, animationSpec)
        }
        animation.animateTo(disabledValue, animationSpec)
    }

    return animation.asState()
}

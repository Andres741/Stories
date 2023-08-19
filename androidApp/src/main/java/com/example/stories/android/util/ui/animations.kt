package com.example.stories.android.util.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import com.example.stories.infrastructure.collections.asInfiniteSequence

@Composable
fun actionableFloatAnimation(
    isActive: Boolean,
    disabledValue: Float,
    values: List<Float>,
    animationSpec: AnimationSpec<Float> = tween()
): State<Float> {
    val animation = remember { Animatable(disabledValue) }

    LaunchedEffect(key1 = isActive) {
        if (isActive) values.asInfiniteSequence().forEach {
            animation.animateTo(it, animationSpec)
        }
        else animation.animateTo(disabledValue, animationSpec)
    }

    return animation.asState()
}

package com.example.stories.android.util.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

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

@OptIn(ExperimentalSharedTransitionApi::class)
typealias SharedTransitionStuff = Pair<SharedTransitionScope, AnimatedVisibilityScope>

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedTransition(
    sharedTransitionStuff: SharedTransitionStuff,
    key: String,
) : Modifier {
    return sharedTransitionStuff.first.run {
        sharedElement(
            rememberSharedContentState(key = key),
            animatedVisibilityScope = sharedTransitionStuff.second,
        )
    }
}

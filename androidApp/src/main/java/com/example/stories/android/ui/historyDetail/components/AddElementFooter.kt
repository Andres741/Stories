package com.example.stories.android.ui.historyDetail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditImageElementPopUp
import com.example.stories.android.ui.historyDetail.components.editPopUp.EditTextElementPopUp
import com.example.stories.android.util.resources.getStringResource

@Composable
fun AddElementFooter(
    editMode: Boolean,
    modifier: Modifier = Modifier,
    createTextElement: (text: String) -> Unit,
    createImageElement: (imageUrl: String) -> Unit,
) {
    AnimatedVisibility(
        visible = editMode,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = modifier.padding(vertical = 12.dp),
    ) {
        Box {
            var showNewElementPicker by remember { mutableStateOf(false) }

            val upSpace = animateDpAsState(targetValue = if (showNewElementPicker) 100.dp else 0.dp, label = "").run { { value } }
            val scale = animateFloatAsState(targetValue = if (showNewElementPicker) 30f else 0f, tween(1000), label = "").run { { value } }

            Canvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationY = -upSpace().toPx()
                        scaleX = scale()
                        scaleY = scale()
                    }
                    .clickable { showNewElementPicker = false }
            ) {
                drawCircle(color = Color(0x55555555), radius = 100f)
            }

            var showTextPopUp by remember { mutableStateOf(false) }
            if (showTextPopUp) {
                EditTextElementPopUp(
                    text = "",
                    onConfirm = {
                        createTextElement(it)
                        showTextPopUp = false
                    },
                    onDismiss = { showTextPopUp = false },
                )
            }

            var showImagePopUp by remember { mutableStateOf(false) }
            if (showImagePopUp) {
                EditImageElementPopUp(
                    imageUrl = "",
                    onConfirm = {
                        createImageElement(it)
                        showImagePopUp = false
                    },
                    onDismiss = { showImagePopUp = false },
                )
            }

            AnimatedContent(
                targetState = showNewElementPicker,
                transitionSpec = {
                    if (showNewElementPicker) {
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> height } + fadeOut()
                    } else {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    }
                },
                modifier = Modifier
                    .graphicsLayer {
                        translationY = -upSpace().toPx()
                    },
                label = "",
            ) { showPicker  ->
                if (showPicker) {
                    NewElementPicker(
                        showTextPopUp = {
                            showTextPopUp = true
                            showNewElementPicker = false
                        },
                        showImagePopUp = {
                            showImagePopUp = true
                            showNewElementPicker = false
                        },
                        hide = { showNewElementPicker = false }
                    )
                } else {
                    Button(onClick = { showNewElementPicker = !showNewElementPicker },) {
                        Text(getStringResource { add_item })
                    }
                }
            }
        }
    }
}

@Composable
fun NewElementPicker(
    showTextPopUp: () -> Unit,
    showImagePopUp: () -> Unit,
    hide: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddElementButton(
                onClick = showTextPopUp,
                text = getStringResource { add_text }
            )
            AddElementButton(
                onClick = showImagePopUp,
                text = getStringResource { add_image }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(onClick = hide) {
                Text(text = getStringResource { dismiss })
            }
        }
    }
}

@Composable
fun AddElementButton(
    onClick: () -> Unit,
    text: String,
) {
    TextButton(onClick) {
        Text(text = text, modifier = Modifier.weight(1f))
        Icon(Icons.Rounded.AddCircle, "",)
    }
}

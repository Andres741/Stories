package com.example.stories.android.ui.historyDetail.components.editPopUp

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stories.android.util.ui.ImagePicker

@Composable
fun EditTextElementPopUp(
    text: String,
    onConfirm: (text: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var inputText by remember { mutableStateOf(text) }
    val isTextValid by remember { derivedStateOf { inputText.isNotBlank() } }

    BaseEditElementPopUp(
        onConfirm = { onConfirm(inputText) },
        isOnConfirmEnabled = isTextValid,
        modifier = Modifier.height(300.dp),
        onDismiss = onDismiss,
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = 12,
        )
    }
}

@Composable
fun EditImageElementPopUp(
    imageUri: Uri?,
    onSelectImage: (imageUri: Uri?) -> Unit,
    onConfirm: (imageUri: Uri) -> Unit,
    onDismiss: () -> Unit,
) {
    val isImagePiked = imageUri != null
    BaseEditElementPopUp(
        onConfirm = { onConfirm(imageUri ?: return@BaseEditElementPopUp) },
        isOnConfirmEnabled = imageUri != null,
        modifier = Modifier.fillMaxHeight(.8f).fillMaxWidth(),
        onDismiss = onDismiss,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ImagePicker(
                imageUri = imageUri,
                onUriChange = onSelectImage,
                modifier = Modifier.then(
                    if (isImagePiked) Modifier.align(Alignment.Center)
                    else Modifier.align(Alignment.BottomCenter)
                )
            )
        }
    }
}

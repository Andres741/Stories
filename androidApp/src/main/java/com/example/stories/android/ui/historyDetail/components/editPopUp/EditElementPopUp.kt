package com.example.stories.android.ui.historyDetail.components.editPopUp

import androidx.compose.foundation.layout.Spacer
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
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

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
    imageUrl: String,
    onConfirm: (imageUrl: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var inputText by remember { mutableStateOf(imageUrl) }
    var isTextValid by remember { mutableStateOf(false) }

    BaseEditElementPopUp(
        onConfirm = { onConfirm(inputText) },
        isOnConfirmEnabled = isTextValid,
        modifier = Modifier.height(600.dp),
        onDismiss = onDismiss,
    ) {
        AsyncImage(
            model = inputText,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            onState = { isTextValid = it is AsyncImagePainter.State.Success }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 15,
        )
    }
}

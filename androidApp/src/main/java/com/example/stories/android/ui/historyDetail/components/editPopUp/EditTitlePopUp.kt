package com.example.stories.android.ui.historyDetail.components.editPopUp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditTitlePopUp(
    title: String,
    onConfirm: (title: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var inputText by remember { mutableStateOf(title) }
    val isTextValid by remember { derivedStateOf { inputText.isNotBlank() } }

    BaseEditElementPopUp(
        onConfirm = { onConfirm(inputText) },
        isOnConfirmEnabled = isTextValid,
        modifier = Modifier.height(150.dp),
        onDismiss = onDismiss,
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = 15,
        )
    }
}

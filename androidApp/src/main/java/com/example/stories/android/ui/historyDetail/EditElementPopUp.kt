package com.example.stories.android.ui.historyDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.data.domain.model.Element

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseEditElementPopUp(
    onConfirm: () -> Unit,
    isOnConfirmEnabled: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier
                .padding(8.dp)
                .then(modifier)
            ) {

                content()

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(getStringResource { dismiss })
                    }
                    TextButton(
                        onClick = onConfirm,
                        enabled = isOnConfirmEnabled,
                    ) {
                        Text(getStringResource { accept })
                    }
                }
            }
        }
    }
}

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

@Composable
fun EditTextElementPopUp(
    textElement: Element.Text,
    onConfirm: (textElement: Element.Text) -> Unit,
    onDismiss: () -> Unit,
) {
    var inputText by remember { mutableStateOf(textElement.text) }
    val isTextValid by remember { derivedStateOf { inputText.isNotBlank() } }

    BaseEditElementPopUp(
        onConfirm = { onConfirm(textElement.copy(text = inputText)) },
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
    imageElement: Element.Image,
    onConfirm: (imageElement: Element.Image) -> Unit,
    onDismiss: () -> Unit,
) {
    var inputText by remember { mutableStateOf(imageElement.imageResource) }
    var isTextValid by remember { mutableStateOf(false) }

    BaseEditElementPopUp(
        onConfirm = { onConfirm(imageElement.copy(imageResource = inputText)) },
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

@Composable
fun EditElementPopUp(
    historyElement: Element,
    onConfirm: (historyElement: Element) -> Unit,
    onDismiss: () -> Unit,
) {
    when (historyElement) {
        is Element.Text -> EditTextElementPopUp(historyElement, onConfirm, onDismiss)
        is Element.Image -> EditImageElementPopUp(historyElement, onConfirm, onDismiss)
    }
}

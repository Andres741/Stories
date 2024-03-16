package com.example.stories.android.util.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stories.android.util.resources.getStringResource

@Composable
fun ImagePicker(
    imageUri: Uri?,
    onUriChange: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        onUriChange(uri ?: return@rememberLauncherForActivityResult)
    }

    AnimatedContent(targetState = imageUri, label = "", modifier = modifier) { uri ->
        if (uri != null) {
            ItemCard {
                AsyncImage(model = uri, contentDescription = "")

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    ) {
                        Text(text = getStringResource { pick_other_image })
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onUriChange(null) },
                    ) {
                        Text(text = getStringResource { drop_image })
                    }
                }
            }
        } else {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            ) {
                Text(text = getStringResource { pick_image })
            }
        }
    }
}

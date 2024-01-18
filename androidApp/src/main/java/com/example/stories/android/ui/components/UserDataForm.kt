package com.example.stories.android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.stories.android.util.resources.getStringResource

@Composable
fun UserDataForm(
    name: String,
    isNameValid: Boolean,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    image: String,
    onImageChange: (String) -> Unit,
    onURLValid: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(36.dp),
        modifier = Modifier
            .padding(vertical = 12.dp)
            .verticalScroll(rememberScrollState())
            .then(modifier)
    ) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            label = { Text(text = getStringResource { user_name }) },
            leadingIcon = { Icon(Icons.Filled.AccountBox, "") },
            isError = isNameValid,
            supportingText = if (isNameValid) {
                { Text(text = getStringResource { user_name_not_valid_warn }) }
            } else null,
        )

        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 10,
            label = { Text(text = getStringResource { user_description }) },
            leadingIcon = { Icon(Icons.Filled.Menu, "") },
        )

        TextField(
            value = image,
            onValueChange = onImageChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            label = { Text(text = getStringResource { user_profile_image }) },
            leadingIcon = { Icon(Icons.Filled.Face, "") },
            supportingText = { Text(text = getStringResource { not_loaded_profile_image_warn }) },
        )

        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(.7f)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            onState = { onURLValid(it is AsyncImagePainter.State.Success) },
        )
    }
}

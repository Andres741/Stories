package com.example.stories.android.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.DefaultLoadingScreen
import com.example.stories.viewModel.UserCreationState

@Composable
fun UserDataEditor(
    title: String,
    acceptText: String,
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    image: String,
    onImageChange: (String) -> Unit,
    isURLValid: Boolean,
    onURLValid: (Boolean) -> Unit,
    userCreationState: UserCreationState,
    summitUserData: (name: String, description: String, profileImage: String?) -> Unit,
    onUserCreated: () -> Unit,
) {

    LaunchedEffect(
        key1 = userCreationState,
        block = {
            if (userCreationState is UserCreationState.Created) onUserCreated()
        },
    )

    Box {
        AnimatedVisibility(
            visible = userCreationState is UserCreationState.CreatingUser,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            DefaultLoadingScreen()
        }

        val blurRadius by animateDpAsState(
            targetValue = if (userCreationState is UserCreationState.CreatingUser) 10.dp else 0.dp,
            label = "",
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurRadius)
                .padding(horizontal = 16.dp)
        ) {
            TitleText(text = title)

            UserDataForm(
                name = name,
                isNameValid = userCreationState !is UserCreationState.NotValidName,
                onNameChange = onNameChange,
                description = description,
                onDescriptionChange = onDescriptionChange,
                image = image,
                onImageChange = onImageChange,
                onURLValid = { onURLValid(it) },
                modifier = Modifier.weight(1f),
            )

            Button(
                onClick = { summitUserData(name, description, image.takeIf { isURLValid }) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            ) {
                Text(text = acceptText)
            }
        }
    }
}

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
            isError = !isNameValid,
            supportingText = if (!isNameValid) {
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

@Preview(widthDp = 400, heightDp = 800)
@Composable
fun UserDataEditor_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val (name, onNameChange) = remember { mutableStateOf("") }
            val (description, onDescriptionChange) = remember { mutableStateOf("") }
            val (image, onImageChange) = remember { mutableStateOf("") }
            val (isURLValid, onURLValid) = remember { mutableStateOf(false) }

            var userCreationState by remember { mutableStateOf(UserCreationState.None as UserCreationState) }
            val context = LocalContext.current

            UserDataEditor(
                title = "UserDataEditor",
                acceptText = "next state",
                name = name,
                onNameChange = onNameChange,
                description = description,
                onDescriptionChange = onDescriptionChange,
                image = image,
                onImageChange = onImageChange,
                isURLValid = isURLValid,
                onURLValid = onURLValid,
                userCreationState = userCreationState,
                summitUserData = { _, _, _ -> userCreationState = userCreationState.nextState() },
                onUserCreated = { Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}

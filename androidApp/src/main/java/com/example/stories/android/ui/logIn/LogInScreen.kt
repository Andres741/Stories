package com.example.stories.android.ui.logIn

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.ui.components.UserDataForm
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.DefaultLoadingScreen
import com.example.stories.viewModel.UserCreationState

@Composable
fun LogInScreen(
    viewModel: LogInViewModel,
    navigateBack: () -> Unit,
) {
    val userCreationState by viewModel.userCreationState.collectAsStateWithLifecycle()

    LogIn(
        userCreationState = userCreationState,
        summitUserData = viewModel::summitUserData,
        onUserCreated = navigateBack
    )
}

@Composable
fun LogIn(
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
            TitleText(text = getStringResource { log_in })

            val (name, onNameChange) = remember { mutableStateOf("") }
            val (description, onDescriptionChange) = remember { mutableStateOf("") }
            val (image, onImageChange) = remember { mutableStateOf("") }
            var isURLValid by remember { mutableStateOf(false) }

            UserDataForm(
                name = name,
                isNameValid = userCreationState !is UserCreationState.NotValidName,
                onNameChange = onNameChange,
                description = description,
                onDescriptionChange = onDescriptionChange,
                image = image,
                onImageChange = onImageChange,
                onURLValid = { isURLValid = it },
                modifier = Modifier.weight(1f),
            )

            Button(
                onClick = { summitUserData(name, description, image.takeIf { isURLValid }) },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
            ) {
                Text(text = getStringResource { crate_user })
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 800)
@Composable
fun LogIn_preview() {
    StoriesTheme {
        var userCreationState by remember { mutableStateOf(UserCreationState.None as UserCreationState) }
        val context = LocalContext.current

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LogIn(
                userCreationState = userCreationState,
                summitUserData = { _, _, _ ->
                    userCreationState = userCreationState.nextState()
                },
                onUserCreated = {
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }
}

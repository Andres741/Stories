package com.example.stories.android.ui.logIn

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.UserDataEditor
import com.example.stories.android.util.resources.getStringResource
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

    val (name, onNameChange) = remember { mutableStateOf("") }
    val (description, onDescriptionChange) = remember { mutableStateOf("") }
    val (image, onImageChange) = remember { mutableStateOf("") }
    val (isURLValid, onURLValid) = remember { mutableStateOf(false) }

    UserDataEditor(
        title = getStringResource { log_in },
        acceptText = getStringResource { crate_user },
        name = name,
        onNameChange = onNameChange,
        description = description,
        onDescriptionChange = onDescriptionChange,
        image = image,
        onImageChange = onImageChange,
        isURLValid = isURLValid,
        onURLValid = onURLValid,
        userCreationState = userCreationState,
        summitUserData = summitUserData,
        onUserCreated = onUserCreated
    )
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

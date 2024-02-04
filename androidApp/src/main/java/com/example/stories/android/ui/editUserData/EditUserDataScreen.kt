package com.example.stories.android.ui.editUserData

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.UserDataEditor
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User
import com.example.stories.viewModel.UserCreationState

@Composable
fun EditUserDataScreen(
    viewModel: EditUserDataScreenViewModel,
    navigateBack: () -> Unit
) {
    val localUserLoadStatus by viewModel.localUserLoadStatus.collectAsStateWithLifecycle()
    LoadingDataScreen(loadStatus = localUserLoadStatus) { user ->
        val userCreationState by viewModel.userCreationState.collectAsStateWithLifecycle()
        EditUserData(
            user = user,
            userCreationState = userCreationState,
            summitUserData = viewModel::saveNewUserData,
            onUserCreated = navigateBack,
        )
    }
}

@Composable
fun EditUserData(
    user: User,
    userCreationState: UserCreationState,
    summitUserData: (name: String, description: String, profileImage: String?) -> Unit,
    onUserCreated: () -> Unit,
) {
    val (name, onNameChange) = remember { mutableStateOf(user.name) }
    val (description, onDescriptionChange) = remember { mutableStateOf(user.description) }
    val (image, onImageChange) = remember { mutableStateOf(user.profileImage ?: "") }
    val (isURLValid, onURLValid) = remember { mutableStateOf(user.profileImage != null) }

    UserDataEditor(
        title = getStringResource { edit_user_data_title },
        acceptText = getStringResource { edit_user_data_save_new_data },
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
        onUserCreated = onUserCreated,
    )
}

@Preview
@Composable
fun EditUserData_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EditUserData(
                user = HistoryMocks().getMockUsers()[0],
                userCreationState = UserCreationState.None,
                onUserCreated = {},
                summitUserData = { _, _, _ -> }
            )
        }
    }
}

package com.example.stories.android.ui.userData

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.ui.components.TitleText
import com.example.stories.android.util.resources.getStringResource
import com.example.stories.android.util.resources.getPainterResource
import com.example.stories.android.util.ui.LoadingDataScreen
import com.example.stories.model.domain.model.HistoryMocks
import com.example.stories.model.domain.model.User

@Composable
fun UserDataScreen(
    viewModel: UserDataViewModel,
    navigateEditUser: () -> Unit,
) {
    val userLoadStatus by viewModel.userLoadStatus.collectAsStateWithLifecycle()

    LoadingDataScreen(userLoadStatus) { user ->
        UserData(
            user = user,
            navigateEditUser = navigateEditUser
        )
    }
}

@Composable
fun UserData(
    user: User,
    navigateEditUser: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateEditUser) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = getPainterResource { edit_icon },
                    contentDescription = "",
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TitleText(text = getStringResource { user_data_title })

            val imageModifier = Modifier
                .size(160.dp)
                .clip(CircleShape)

            user.profileImage?.let { image ->
                AsyncImage(
                    model = image,
                    contentDescription = "",
                    modifier = imageModifier
                )
            } ?: run {
                Image(
                    painter = getPainterResource { no_profile_image_icon },
                    contentDescription = "",
                    modifier = imageModifier
                )
            }

            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(user.description)
        }
    }
}

@Preview
@Composable
fun UserData_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserData(
                user = HistoryMocks().getMockUsers()[0].copy(profileImage = null),
                navigateEditUser = {},
            )
        }
    }
}

package com.example.stories.android.util.ui.test

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.ImageUtils
import com.example.stories.android.util.rememberRefreshableAsyncImagePainter
import com.example.stories.android.util.ui.ImagePicker
import com.example.stories.viewModel.TestCommonViewModel
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch

@Composable
fun TestImagePickerScreen(viewModel: TestViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            val context = LocalContext.current
            val imagesSent by viewModel.imagesSent.collectAsStateWithLifecycle()

            var imageUri by remember { mutableStateOf(null as Uri?) }

            Text(text = "Images sent: $imagesSent")

            ImagePicker(
                imageUri = imageUri,
                onUriChange = { imageUri = it },
            )

            imageUri?.let { image ->
                Button(
                    onClick = { viewModel.sendPhoto(image, context) }
                ) {
                    Text(text = "Send image")
                }
            }

            Divider()

            var refreshIndex by remember { mutableIntStateOf(0) }
            val painter = rememberRefreshableAsyncImagePainter(IMAGE_URL, refreshIndex)

            Text(text = "Image from service")

            Button(onClick = { refreshIndex++ }) {
                Text(text = "Reload")
            }

            Image(
                painter = painter,
                contentDescription = "",
            )
        }
    }
}

const val IMAGE_URL = "http://192.168.1.137:8080/api/images/testImage.jpeg"

class TestViewModel : ViewModel() {
    private val commonViewModel = TestCommonViewModel(coroutineScope = viewModelScope)
    val imagesSent get() = commonViewModel.imagesSent
    fun sendPhoto(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val byteArrayImage = ImageUtils.uriToImageDomain(
                uri = imageUri,
                context = context,
            ) ?: return@launch

            commonViewModel.sendPhoto(byteArrayImage)
        }
    }
}

@Preview
@Composable
private fun TestImagePickerScreen_prev() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TestImagePickerScreen()
        }
    }
}

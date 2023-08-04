package com.example.stories.android.util.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.stories.android.ui.StoriesTheme

@Composable
fun AsyncItemImage(url: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        var isLoading by remember { mutableStateOf(true) }

        if (isLoading) CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            strokeCap = StrokeCap.Round
        )

        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
            onState = { isLoading = it is AsyncImagePainter.State.Loading }
        )
    }
}

@Preview
@Composable
fun AsyncItemImage_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(verticalArrangement = Arrangement.Center) {
                item {
                    AsyncItemImage(url = "https://www.elmueble.com/medio/2023/02/26/perro-de-raza-shiba-inu_b6387407_230226130353_900x900.jpg")
                }
            }
        }
    }
}

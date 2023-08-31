package com.example.stories.android.util.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stories.android.ui.StoriesTheme
import com.example.stories.android.util.resources.sharedPainterResource

@Composable
fun EmptyScreen(
    title: String,
    text: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24f.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Icon(sharedPainterResource { empty_list }, "")
        Text(text = title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun EmptyScreen_preview() {
    StoriesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EmptyScreen(
                title = "Example text",
                text = "When talking about declarative UI we first have to know that it’s a programming paradigm that focuses on describing the desired result and what it should look like to the user at a given application state."
            )
        }
    }
}
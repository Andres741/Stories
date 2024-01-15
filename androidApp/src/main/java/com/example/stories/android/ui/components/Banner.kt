package com.example.stories.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Banner(
    bannerText: String,
    onClickBanner: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .background(color = MaterialTheme.colorScheme.tertiary, shape = MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable { onClickBanner() },
    ) {
        Text(
            text = bannerText,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onTertiary,
        )

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "",
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.onTertiary,
        )
    }
}

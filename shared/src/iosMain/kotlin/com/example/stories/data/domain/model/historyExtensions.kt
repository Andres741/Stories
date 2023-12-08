package com.example.stories.data.domain.model

import com.example.stories.model.domain.model.HistoryElement

fun HistoryElement.Text.updateText(new: String) = copy(text = new)
fun HistoryElement.Image.updateImageResource(new: String) = copy(imageResource = new)

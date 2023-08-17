package com.example.stories.data.domain.model

fun Element.Text.updateText(new: String) = copy(text = new)
fun Element.Image.updateImageResource(new: String) = copy(imageResource = new)

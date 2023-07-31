package com.example.stories.infrastructure

import dev.icerock.moko.resources.StringResource

sealed interface TextContainer

data class StringResourceContainer(val resId: StringResource): TextContainer
data class StringContainer(val text: String): TextContainer

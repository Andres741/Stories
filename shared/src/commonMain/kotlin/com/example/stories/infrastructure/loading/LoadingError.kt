package com.example.stories.infrastructure.loading

import com.example.stories.SharedRes
import com.example.stories.infrastructure.StringResourceContainer
import com.example.stories.infrastructure.TextContainer
import dev.icerock.moko.resources.ImageResource

sealed class LoadingError {
    abstract val icon: ImageResource
    abstract val title: TextContainer
    abstract val message: TextContainer?

    object GenericError : LoadingError() {
        override val icon: ImageResource = SharedRes.images.error_icon
        override val title: TextContainer = StringResourceContainer(SharedRes.strings.generic_error_title)
        override val message: TextContainer = StringResourceContainer(SharedRes.strings.generic_error_message)
    }

    object NoConnectionError : LoadingError() {
        override val icon: ImageResource = SharedRes.images.no_connection_icon
        override val title: TextContainer = StringResourceContainer(SharedRes.strings.no_connection_error_title)
        override val message: TextContainer = StringResourceContainer(SharedRes.strings.no_connection_error_message)
    }

    object ServiceUnavailableError : LoadingError() {
        override val icon: ImageResource = SharedRes.images.error_icon
        override val title: TextContainer = StringResourceContainer(SharedRes.strings.service_unavailable_error_title)
        override val message: TextContainer = StringResourceContainer(SharedRes.strings.service_unavailable_error_message)
    }

    data class CustomError(
        override val icon: ImageResource,
        override val title: TextContainer,
        override val message: TextContainer? = null
    ) : LoadingError()
}

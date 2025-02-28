package com.example.stories.model.domain.model

import com.example.stories.model.dataSource.remote.image.ImagesApi
import com.example.stories.model.dataSource.remote.image.model.ImageResponse

data class ImageUrl(val imageName: String) {
    var url = IMAGES_API_PATH + imageName
}

private const val IMAGES_API_PATH = "${ImagesApi.IMAGES_API}/"

fun ImageResponse.serverImageToUrl() = ImageUrl(imageName)

fun String.toImageUrl() = ImageUrl(this)

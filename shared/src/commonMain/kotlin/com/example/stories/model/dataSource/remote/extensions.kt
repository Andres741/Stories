package com.example.stories.model.dataSource.remote

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

inline fun <reified T> HttpRequestBuilder.setJsonBody(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}

fun createJpegImageFormData(key: String, imageData: ByteArray?, imageName: String = "image", block: FormBuilder.() -> Unit = {}) = formData {
    if (imageData == null) return@formData
    append(key, imageData, Headers.build {
        append(HttpHeaders.ContentType, "image/jpeg")
        append(HttpHeaders.ContentDisposition, "filename=\"$imageName.jpeg\"")
    })
    block()
}

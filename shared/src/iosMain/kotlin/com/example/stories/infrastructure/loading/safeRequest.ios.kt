package com.example.stories.infrastructure.loading

import io.ktor.client.engine.darwin.DarwinHttpRequestException

actual fun<T> Result<T>.interceptConnectionException(): Result<T> {
    onFailure { throwable ->
        if (throwable is DarwinHttpRequestException) return disconnectedExceptionResult
    }
    return this
}

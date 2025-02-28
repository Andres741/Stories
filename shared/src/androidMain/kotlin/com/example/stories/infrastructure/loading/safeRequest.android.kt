package com.example.stories.infrastructure.loading

import java.net.ConnectException

actual fun<T> Result<T>.interceptConnectionException(): Result<T> {
    onFailure { throwable ->
        if (throwable is ConnectException) return disconnectedExceptionResult
    }
    return this
}

package com.example.stories.infrastructure.loading

object DisconnectedException : Exception()

val disconnectedExceptionResult = Result.failure<Nothing>(DisconnectedException)

expect fun<T> Result<T>.interceptConnectionException(): Result<T>

suspend inline fun<T> safeRequest(crossinline block: suspend () -> T): Result<T> {
    return runCatching { block() }.interceptConnectionException()
}

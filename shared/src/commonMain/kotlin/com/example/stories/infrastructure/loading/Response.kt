package com.example.stories.infrastructure.loading

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

typealias Response<T> = Either<LoadingError, T>

fun<T> Result<T>.toResponse(): Response<T> = fold(
    onSuccess = { it.right() },
    onFailure = { it.mapException() },
)

inline fun<T, R> Result<T>.mapToResponse(transform: (value: T) -> R): Response<R> = fold(
    onSuccess = { transform(it).right() },
    onFailure = { it.mapException() },
)

fun Throwable.mapException() = when (this) {
    DisconnectedException -> LoadingError.NoConnectionError
    is ClientRequestException -> LoadingError.GenericError
    is ServerResponseException -> LoadingError.GenericError
    else -> LoadingError.GenericError
}.left()

fun<T> Response<T>.successOrThrowLoadingError() = fold(
    ifRight = { it },
    ifLeft = { throw Exception("Loading error has occurred:\n$it") },
)

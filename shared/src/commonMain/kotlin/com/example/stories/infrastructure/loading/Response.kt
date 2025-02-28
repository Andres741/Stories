package com.example.stories.infrastructure.loading

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

typealias Response<T> = Either<LoadingError, T>

fun<T> Result<T>.toResponse(): Response<T> = fold(
    onSuccess = { it.right() },
    onFailure = { t ->
        when (t) {
            is DisconnectedException -> LoadingError.NoConnectionError
            is ClientRequestException -> LoadingError.GenericError
            is ServerResponseException -> LoadingError.GenericError
            else -> LoadingError.GenericError
        }.left()
    },
)

inline fun<T, R> Result<T>.mapToResponse(transform: (value: T) -> R): Response<R> = fold(
    onSuccess = { transform(it).right() },
    onFailure = { t ->
        when (t) {
            is DisconnectedException -> LoadingError.NoConnectionError
            is ClientRequestException -> LoadingError.GenericError
            is ServerResponseException -> LoadingError.GenericError
            else -> LoadingError.GenericError
        }.left()
    },
)

package com.example.stories.infrastructure.loading

sealed class LoadStatus<out D: Any> {
    data class Data<T: Any>(val value: T) : LoadStatus<T>()
    data class Error(val error: LoadingError) : LoadStatus<Nothing>()
    object Loading : LoadStatus<Nothing>()

    inline fun<T> fold(
        onSuccess: (D) -> T,
        onError: (LoadingError) -> T,
        onLoading: () -> T
    ): T = when (this) {
        is Data -> onSuccess(value)
        is Error -> onError(error)
        Loading -> onLoading()
    }

    fun dataOrNull(): D? = (this as? Data)?.value

    fun errorOrNull(): LoadingError? = (this as? Error)?.error

    fun isLoading(): Boolean = this is Loading

    fun<T: Any> mapData(transform: (D) -> T): LoadStatus<T> {
        return when (this) {
            is Data -> Data(transform(value))
            is Error -> this
            is Loading -> this
        }
    }
}

fun<T: Any> loadStatusOf(data: T) = LoadStatus.Data(data)

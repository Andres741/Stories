package com.example.stories.infrastructure.loading

import com.example.stories.infrastructure.coroutines.flow.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed class LoadStatus<out D: Any> {
    data class Data<T: Any>(
        val value: T,
        val isOnRefreshing: Boolean = false,
    ) : LoadStatus<T>() {
        inline fun<V: Any> mapValue(transform: (T) -> V) = Data(
            value = transform(value),
            isOnRefreshing = isOnRefreshing,
        )
        fun setRefreshing() = copy(isOnRefreshing = true)
    }

    data class Error(val error: LoadingError) : LoadStatus<Nothing>()
    object Loading : LoadStatus<Nothing>()

    inline fun<T> fold(
        onSuccess: (D) -> T,
        onError: (LoadingError) -> T,
        onLoading: () -> T
    ): T = when (this) {
        is Data -> onSuccess(value)
        is Error -> onError(error)
        is Loading -> onLoading()
    }

    fun dataOrNull(): D? = (this as? Data)?.value

    fun errorOrNull(): LoadingError? = (this as? Error)?.error

    fun isLoading(): Boolean = this is Loading

    fun isRefreshing(): Boolean = (this as? Data)?.isOnRefreshing == true

    inline fun<T: Any> mapData(transform: (D) -> T): LoadStatus<T> {
        return when (this) {
            is Data -> mapValue(transform)
            is Error -> this
            is Loading -> this
        }
    }

    fun<T: Any> Result<T>.toLoadStatus(exceptionMapper: ((Exception) -> LoadingError)? = null) = fold(
        onSuccess = { Data(it) },
        onFailure = { throwable ->
            if (throwable is kotlin.Error) throw throwable
            if (throwable is Exception && exceptionMapper != null) {
                return@fold Error(exceptionMapper(throwable))
            }
            Error(LoadingError.GenericError)
        },
    )
}

fun<T: Any> loadStatusOf(data: T) = LoadStatus.Data(data)

fun <T: Any> MutableStateFlow<LoadStatus<T>>.setRefreshing() {
    update { oldValue ->
        when (oldValue) {
            is LoadStatus.Data -> {
                oldValue.setRefreshing()
            }
            else -> {
                LoadStatus.Loading
            }
        }
    }
}

fun <T: Any> Flow<LoadStatus<T>>.toLoadStatusStateFlow(scope: CoroutineScope): StateFlow<LoadStatus<T>> = stateIn(
    scope, SharingStarted.WhileSubscribed(1000), LoadStatus.Loading
)

fun <T: Any> Flow<LoadStatus<T>>.toLoadStatusCommonStateFlow(scope: CoroutineScope) =
    toLoadStatusStateFlow(scope).toCommonStateFlow()

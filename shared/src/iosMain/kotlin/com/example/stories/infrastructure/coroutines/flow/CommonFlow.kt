package com.example.stories.infrastructure.coroutines.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

actual open class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow {

    fun subscribe(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        onCollect: (T) -> Unit,
    ): DisposableHandle {
        val job = scope.launch(dispatcher) {
            flow.collect(onCollect)
        }
        return DisposableHandle { job.cancel() }
    }

    fun subscribe(
        scope: CoroutineScope,
        onCollect: (T) -> Unit,
    ): DisposableHandle {
        return subscribe(
            scope = scope,
            dispatcher = Dispatchers.Main,
            onCollect = onCollect,
        )
    }

    fun subscribeForever(
        onCollect: (T) -> Unit,
    ): DisposableHandle {
        return subscribe(
            scope = GlobalScope,
            dispatcher = Dispatchers.Main,
            onCollect = onCollect
        )
    }
}

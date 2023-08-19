package com.example.stories.infrastructure.collections

fun<T> List<T>.asInfiniteSequence(): Sequence<T> = sequence {
    while (true) yieldAll(this@asInfiniteSequence)
}

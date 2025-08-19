package com.example.stories.testUtil

class InvocationCounter(val invocationsTarget: Int) {
    var invocationsCount: Int = 0
        private set

    operator fun invoke() {
        invocationsCount++
        if (invocationsCount > invocationsTarget) {
            throw AssertionError("Invocation limit exceeded, max invocations: $invocationsTarget, actual invocations: $invocationsCount")
        }
    }

    fun verifyInvocations() {
        if (invocationsCount != invocationsTarget) {
            throw AssertionError("Invocation target not reached, expected invocations: $invocationsTarget, actual invocations: $invocationsCount")
        }
    }

    inline fun <T> count(block: () -> T): T {
        invoke()
        return block()
    }

    inline fun <T> use(block: (InvocationCounter) -> T): T {
        val result = block(this)
        verifyInvocations()
        return result
    }
}

inline fun<T> use(
    c0: InvocationCounter,
    c1: InvocationCounter,
    block: (InvocationCounter, InvocationCounter) -> T
): T {
    val result = block(c0, c1)
    c0.verifyInvocations()
    c1.verifyInvocations()
    return result
}

inline fun<T> use(
    c0: InvocationCounter,
    c1: InvocationCounter,
    c2: InvocationCounter,
    block: (InvocationCounter, InvocationCounter, InvocationCounter) -> T
): T {
    val result = block(c0, c1, c2)
    c0.verifyInvocations()
    c1.verifyInvocations()
    c2.verifyInvocations()
    return result
}

inline fun<T> use(
    c0: InvocationCounter,
    c1: InvocationCounter,
    c2: InvocationCounter,
    c3: InvocationCounter,
    block: (InvocationCounter, InvocationCounter, InvocationCounter, InvocationCounter) -> T
): T {
    val result = block(c0, c1, c2, c3)
    c0.verifyInvocations()
    c1.verifyInvocations()
    c2.verifyInvocations()
    c3.verifyInvocations()
    return result
}

inline fun<T> use(
    c0: InvocationCounter,
    c1: InvocationCounter,
    c2: InvocationCounter,
    c3: InvocationCounter,
    c4: InvocationCounter,
    block: (InvocationCounter, InvocationCounter, InvocationCounter, InvocationCounter, InvocationCounter) -> T
): T {
    val result = block(c0, c1, c2, c3, c4)
    c0.verifyInvocations()
    c1.verifyInvocations()
    c2.verifyInvocations()
    c3.verifyInvocations()
    c4.verifyInvocations()
    return result
}

inline fun<T> use(counters: List<InvocationCounter>, block: (List<InvocationCounter>) -> T): T {
    val result = block(counters)
    counters.forEach(InvocationCounter::verifyInvocations)
    return result
}


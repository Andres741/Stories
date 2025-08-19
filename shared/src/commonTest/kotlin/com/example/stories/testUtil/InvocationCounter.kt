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

    inline fun <T> use(block: (InvocationCounter) -> T): T {
        val result = block(this)
        verifyInvocations()
        return result
    }
}

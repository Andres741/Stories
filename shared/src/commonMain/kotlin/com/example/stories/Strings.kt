package com.example.stories

import dev.icerock.moko.resources.StringResource

expect class Strings {
    fun get(resId: StringResource): String
    fun get(resId: StringResource, args: List<Any>): String
}
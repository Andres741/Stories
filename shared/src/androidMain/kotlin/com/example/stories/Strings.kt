package com.example.stories

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

actual class Strings(private val context: Context) {
    actual fun get(resId: StringResource): String = StringDesc.Resource(resId).toString(context)
    actual fun get(resId: StringResource, args: List<Any>): String = resId.format(*args.toTypedArray()).toString(context)
}
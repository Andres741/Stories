package com.example.stories

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

actual class Strings {
    actual fun get(resId: StringResource): String = StringDesc.Resource(resId).localized()
    actual fun get(resId: StringResource, args: List<Any>): String = resId.format(*args.toTypedArray()).localized()
}
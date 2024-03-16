package com.example.stories.model.domain.model

data class ImageDomain(
    val name: String,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageDomain) return false

        if (name != other.name) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}

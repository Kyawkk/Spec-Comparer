package com.kyawzinlinn.speccomparer.utils

object ImageUrlBuilder {
    fun build(path: String) : String {
        return IMG_PREFIX + path
    }
}
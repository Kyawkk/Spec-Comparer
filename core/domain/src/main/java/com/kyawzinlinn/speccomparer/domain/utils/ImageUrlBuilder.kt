package com.kyawzinlinn.speccomparer.domain.utils

object ImageUrlBuilder {
    fun build(path: String) : String {
        return IMG_PREFIX + path
    }

    fun buildSingleImage(path: String) : String{
        return "$IMAGE_URL$path-mini.jpeg"
    }
}
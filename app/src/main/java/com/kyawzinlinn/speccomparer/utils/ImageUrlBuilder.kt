package com.kyawzinlinn.speccomparer.utils

import android.util.Log

object ImageUrlBuilder {
    fun build(path: String) : String {
        return IMG_PREFIX + path
    }

    fun buildSingleImage(path: String) : String{
        return IMAGE_URL + path + "-mini.jpeg"
    }
}
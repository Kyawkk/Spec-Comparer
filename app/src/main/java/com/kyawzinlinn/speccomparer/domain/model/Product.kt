package com.kyawzinlinn.speccomparer.domain.model

import com.kyawzinlinn.speccomparer.utils.ImageUrlBuilder
import com.kyawzinlinn.speccomparer.utils.toParameter

data class Product(
    val content_type: String,
    val name: String,
    val imageUrl : String = "",
)

fun List<Product>.addImageLinks() : List<Product> {
    return map {
        it.copy(imageUrl = ImageUrlBuilder.buildSingleImage(it.content_type + "/" + it.name.toParameter()))
    }
}
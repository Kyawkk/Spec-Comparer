package com.kyawzinlinn.speccomparer.domain.model

import com.kyawzinlinn.speccomparer.domain.utils.ImageUrlBuilder
import com.kyawzinlinn.speccomparer.domain.utils.toPath
import com.kyawzinlinn.speccomparer.domain.utils.toProductType

data class Product(
    val content_type: String = "",
    val name: String = "",
    val path: String = "",
    val imageUrl : String = "",
)

fun List<Product>.addImageLinks() : List<Product> {
    return map {
        val imageUrl = ImageUrlBuilder.buildSingleImage(it.content_type.toProductType() , it.name.toPath())
        it.copy(imageUrl = imageUrl, path = it.name.toPath())
    }
}
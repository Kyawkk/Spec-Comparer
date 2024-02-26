package com.kyawzinlinn.speccomparer.domain.model.detail

data class ProductSpecification(
    val productName: String = "",
    val productImageUrl : String = "",
    val productDetails : List<ProductDetail> = emptyList()
)

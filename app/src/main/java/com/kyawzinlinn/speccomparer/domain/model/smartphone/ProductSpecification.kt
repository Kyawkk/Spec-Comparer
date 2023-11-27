package com.kyawzinlinn.speccomparer.domain.model.smartphone

data class ProductSpecification(
    val productName: String,
    val productImageUrl : String,
    val productDetails : List<ProductDetail>
)

package com.kyawzinlinn.speccomparer.domain.model.detail

data class ProductSpecificationResponse(
    val productSpecification: ProductSpecification = ProductSpecification(),
    val productSpecifications : List<SpecificationItem> = emptyList()
)

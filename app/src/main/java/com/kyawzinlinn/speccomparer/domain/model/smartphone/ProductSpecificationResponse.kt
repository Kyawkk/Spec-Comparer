package com.kyawzinlinn.speccomparer.domain.model.smartphone

data class ProductSpecificationResponse(
    val productSpecification: ProductSpecification,
    val productSpecifications : List<SpecificationItem>
)

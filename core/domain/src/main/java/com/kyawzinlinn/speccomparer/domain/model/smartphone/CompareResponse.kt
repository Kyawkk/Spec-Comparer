package com.kyawzinlinn.speccomparer.domain.model.smartphone

data class CompareResponse(
    val headerData: HeaderData,
    val keyDifferences: List<KeyDifference>,
    val compares : List<SpecificationItem>
)

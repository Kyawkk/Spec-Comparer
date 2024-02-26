package com.kyawzinlinn.speccomparer.domain.model.detail

data class CompareResponse(
    val headerData: HeaderData,
    val keyDifferences: List<KeyDifference>,
    val compares : List<SpecificationItem>
)

package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareResponse(
    val compareDeviceHeaderDetails: CompareScore? = null,
    val keyDifferences: CompareKeyDifferences? = null,
    val compareSpecDetails: List<CompareDetailResponse?> = emptyList()
)

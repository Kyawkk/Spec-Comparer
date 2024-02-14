package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareResponse(
    val compareDeviceHeaderDetails: CompareScore?,
    val keyDifferences: CompareKeyDifferences?,
    val compareSpecDetails: List<CompareDetailResponse?>
)

package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareDetailResponse(
    val title: String,
    val scoreBars: List<CompareScoreBar?>,
    val scoreRows: List<CompareScoreRow?>
)

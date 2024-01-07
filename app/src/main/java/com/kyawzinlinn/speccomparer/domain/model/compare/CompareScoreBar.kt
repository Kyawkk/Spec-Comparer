package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareScoreBar(
    val name: String,
    val firstSpecName: String,
    val firstSpecValue: String,
    val secondSpecName: String,
    val secondSpecValue: String
)

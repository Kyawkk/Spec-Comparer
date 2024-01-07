package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareKeyDifferences(
    val title: String,
    val firstKeyDifference: KeyDifference,
    val secondDifference: KeyDifference,
)

data class KeyDifference(
    val title: String,
    val pros: List<String>
)
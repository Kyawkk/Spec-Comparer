package com.kyawzinlinn.speccomparer.domain.model.compare

data class CompareScore(
    val firstScore: String,
    val secondScore: String,
    val firstImgUrl: String,
    val secondImgUrl: String,
    val firstDeviceName: String,
    val secondDeviceName: String
)

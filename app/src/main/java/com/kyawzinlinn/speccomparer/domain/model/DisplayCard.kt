package com.kyawzinlinn.speccomparer.domain.model

import androidx.annotation.DrawableRes
import com.kyawzinlinn.speccomparer.utils.ProductType

data class DisplayCard(
    @DrawableRes val icon : Int,
    val title: String,
    val description: String,
    val type: ProductType
)

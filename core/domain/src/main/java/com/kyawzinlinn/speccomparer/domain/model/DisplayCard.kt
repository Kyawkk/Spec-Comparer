package com.kyawzinlinn.speccomparer.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

data class DisplayCard(
    @DrawableRes val icon : Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val type: ProductType
)

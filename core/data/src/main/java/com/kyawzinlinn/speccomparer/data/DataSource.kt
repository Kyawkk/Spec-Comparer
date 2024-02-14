package com.kyawzinlinn.speccomparer.data

import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.HeaderData
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

object DataSource {
    val defaultCompareResponse = CompareResponse(
        headerData = HeaderData("","","",""),
        keyDifferences = emptyList(),
        compares = emptyList()
    )
}
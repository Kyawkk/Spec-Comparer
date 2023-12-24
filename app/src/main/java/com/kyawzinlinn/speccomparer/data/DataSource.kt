package com.kyawzinlinn.speccomparer.data

import com.kyawzinlinn.speccomparer.R
import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.HeaderData
import com.kyawzinlinn.speccomparer.utils.ProductType

object DataSource {
    val defaultCompareResponse = CompareResponse(
        headerData = HeaderData("","","",""),
        keyDifferences = emptyList(),
        compares = emptyList()
    )

    val displayCards = listOf(
        DisplayCard(R.drawable.smartphone,"Smartphones","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Smartphone),
        DisplayCard(R.drawable.smartphone_chip,"Smartphone Processors","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Soc),
        DisplayCard(R.drawable.processor,"Laptop CPUs","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Cpu),
        DisplayCard(R.drawable.laptop,"Laptops","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Laptop),
    )
}
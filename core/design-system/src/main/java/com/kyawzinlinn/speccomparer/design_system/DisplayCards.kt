package com.kyawzinlinn.speccomparer.design_system

import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

val displayCards = listOf(
    DisplayCard(R.drawable.smartphone,R.string.smartphone, R.string.smartphone_description, ProductType.Smartphone),
    DisplayCard(R.drawable.smartphone_chip,R.string.soc, R.string.soc_description, ProductType.Soc),
    DisplayCard(R.drawable.processor,R.string.laptop_cpus, R.string.cpus_description, ProductType.Cpu),
    DisplayCard(R.drawable.laptop,R.string.laptops, R.string.laptop_description, ProductType.Laptop)
)
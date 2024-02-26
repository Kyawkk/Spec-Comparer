package com.kyawzinlinn.speccomparer.domain.utils

enum class ProductType (val title: String) {
    Smartphone ("phone"),
    Soc("soc"),
    Cpu("cpu"),
    Laptop("laptop")
}

fun String.toProductType(): ProductType {
    return ProductType.entries.find { it.title.equals(this,ignoreCase = true) } ?: ProductType.Smartphone
}
package com.kyawzinlinn.speccomparer.domain.utils

enum class ProductType (val title: String) {
    Smartphone ("phone"),
    Soc("soc"),
    Cpu("cpu"),
    Laptop("laptop")
}

fun String.toProductType(): ProductType {
    return ProductType.values().find { it.title.equals(this,ignoreCase = true) } ?: ProductType.Smartphone
}

fun getProductType(value: String) : ProductType {
    return when(value) {
        ProductType.Smartphone.title -> ProductType.Smartphone
        ProductType.Soc.title -> ProductType.Soc
        ProductType.Cpu.title -> ProductType.Cpu
        ProductType.Laptop.title -> ProductType.Laptop
        else -> ProductType.Smartphone
    }
}
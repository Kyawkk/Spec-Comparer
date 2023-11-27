package com.kyawzinlinn.speccomparer.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object JsoupConfig {
    fun connectCompareWebUrl(path: String, productType: ProductType) : Document{
        return when (productType) {
            ProductType.Smartphone -> connect("phone-compare/$path")
            ProductType.Soc -> connect("soc-compare/$path")
            ProductType.Cpu -> connect("cpu-compare/$path")
            ProductType.Laptop -> connect("laptop-compare/$path")
        }
    }

    fun connectProductDetailsWebUrl(path: String, productType: ProductType) : Document{
        return when (productType) {
            ProductType.Smartphone -> connect("phone/$path")
            ProductType.Soc -> connect("soc/$path")
            ProductType.Cpu -> connect("cpu/$path")
            ProductType.Laptop -> connect("laptop/$path")
        }
    }

    private fun connect(path: String) : Document {
        return Jsoup.connect(WEB_URL + path)
            .timeout(10000)
            .get()
    }
}

fun Elements.getAllChildren(): List<String>{
    return this.map { it.text() }
}

fun String.toParameter(): String{
    return if (this.contains(" ")) this.toLowerCase()
        .replace(Regex("[^a-z0-9\\s]"), "")
        .replace(" ", "-") else this
}

fun main() {
    println("Apple iPhone SE (2022)".toParameter())
}
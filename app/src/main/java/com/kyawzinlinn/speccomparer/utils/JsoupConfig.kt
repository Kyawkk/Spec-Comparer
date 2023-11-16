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

    private fun connect(path: String) : Document {
        return Jsoup.connect(WEB_URL + path)
            .timeout(10000)
            .get()
    }
}

fun Elements.getAllChildren(): List<String>{
    return this.map { it.text() }
}
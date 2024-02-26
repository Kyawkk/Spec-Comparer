package com.kyawzinlinn.speccomparer.domain.utils

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.Locale

object JsoupConfig {
    private const val TAG = "JsoupConfig"
    fun connectCompareWebUrl(path: String, productType: ProductType) : Document{
        return connect("${productType.title}-compare/$path")
        /*return when (productType) {
            ProductType.Smartphone -> connect("phone-compare/$path")
            ProductType.Soc -> connect("soc-compare/$path")
            ProductType.Cpu -> connect("cpu-compare/$path")
            ProductType.Laptop -> connect("laptop-compare/$path")
        }*/
    }

    fun connectProductDetailsWebUrl(path: String, productType: ProductType) : Document{
        return connect("${productType.title}/$path")
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

fun String.toPath(): String{
    return if (this.contains(" ")) this.lowercase(Locale.getDefault())
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .replace("\"","")
        .replace(" ", "-") else this
}
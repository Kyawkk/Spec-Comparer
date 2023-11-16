package com.kyawzinlinn.speccomparer.data.remote.api.smartphone

import com.kyawzinlinn.speccomparer.domain.model.smartphone.HeaderData
import com.kyawzinlinn.speccomparer.domain.model.smartphone.KeyDifference
import com.kyawzinlinn.speccomparer.utils.JsoupConfig
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.WEB_URL
import com.kyawzinlinn.speccomparer.utils.getAllChildren
import com.kyawzinlinn.speccomparer.utils.printAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object CompareApi {
    fun compareSmartphones(firstDevice: String, secondDevice: String) {
        val document = JsoupConfig.connectCompareWebUrl(
            "$firstDevice-vs-$secondDevice",
            ProductType.Smartphone
        )
        val cardElements = document.select("body>div.main-container>main>article>div.card")
        val headerElement = cardElements.first()
        val keyDifferencesElement = cardElements.get(1)
        getKeyDifferences(keyDifferencesElement.select("div.card-block").get(1)).forEach {
            println(it)
        }
        //println(getHeaderData(headerElement))
    }

    private fun getHeaderData(element: Element?): HeaderData {
        val imageElements = element?.select("div.card-block>div.compare-head>div.compare-head-item")
        var firstImg = imageElements?.get(0)?.select("a>img")?.attr("src") ?: ""
        var secondImg = imageElements?.get(1)?.select("a>img")?.attr("src") ?: ""

        return HeaderData(firstImg, secondImg)
    }

    private fun getKeyDifferences(element: Element?): List<KeyDifference> {
        val firstTitle = element?.selectFirst("div.title-h4")?.text() ?: ""
        val secondTitle = element?.select("div.title-h4")?.get(1)?.text() ?: ""
        val firstFacts = element?.select("ul")?.get(0)?.select("li")?.getAllChildren() ?: listOf()
        val secondFacts = element?.select("ul")?.get(1)?.select("li")?.getAllChildren() ?: listOf()

        return listOf(
            KeyDifference(firstTitle,firstFacts),
            KeyDifference(secondTitle,secondFacts)
        )
    }
}

fun main() {
    CompareApi.compareSmartphones("xiaomi-redmi-note-11-pro-plus","apple-iphone-11-pro")
}
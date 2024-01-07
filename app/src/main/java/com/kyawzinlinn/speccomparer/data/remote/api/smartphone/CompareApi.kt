package com.kyawzinlinn.speccomparer.data.remote.api.smartphone

import com.kyawzinlinn.speccomparer.domain.model.compare.CompareDetailResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareKeyDifferences
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScore
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreBar
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreRow
import com.kyawzinlinn.speccomparer.domain.model.compare.KeyDifference
import com.kyawzinlinn.speccomparer.utils.JsoupConfig
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.toParameter
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object CompareApi {
    fun compareDevices(
        firstDevice: String,
        secondDevice: String,
        type: ProductType
    ): CompareResponse {
        val document = JsoupConfig.connectCompareWebUrl("${firstDevice.toParameter()}-vs-${secondDevice.toParameter()}", type)

        val cards = document.select("div.card")
        val compareDetailsList = mutableListOf<CompareDetailResponse>()
        var keyDifferences: CompareKeyDifferences? = null

        val compareHeaderDetails = getCompareDevicesDetails(cards.get(0).select("div.compare-head"))

        cards.forEach {
            val title = it.select("div.card-block>div.card-head>h2").text()
            if (title.lowercase().equals("key differences")) {
                keyDifferences = getKeyDifferences(it)
            } else {
                // handle spec data
                val compareSpecificationDetails = getCompareSpecificationDetails(it)
                if (compareSpecificationDetails?.scoreBars!!.isNotEmpty() || compareSpecificationDetails?.scoreRows!!.isNotEmpty()) {
                    compareDetailsList.add(compareSpecificationDetails)
                }
            }
        }

        return CompareResponse(compareHeaderDetails, keyDifferences, compareDetailsList)
    }

    private fun getCompareSpecificationDetails(card: Element?): CompareDetailResponse? {
        val cardBlocks = card?.select("div.card-block")
        val tables = card?.select("table.specs-table")
        val specTitle = cardBlocks?.get(0)?.select("div.card-head")?.text() ?: ""

        val scoreBars = mutableListOf<CompareScoreBar?>()
        val scoreRows = mutableListOf<CompareScoreRow?>()

        cardBlocks?.forEach {
            scoreBars.addAll(getDataFromCompareScoreBars(it.selectFirst("div.two-columns")))
        }

        tables?.forEach {
            scoreRows.addAll(getDataFromCompareTable(it))
        }

        return CompareDetailResponse(specTitle, scoreBars, scoreRows)
    }

    private fun getCompareDevicesDetails(compareHead: Elements?): CompareScore? {
        try {
            val compareHeadItems = compareHead?.get(0)?.select("div.compare-head-item")
            val firstDevice = compareHeadItems?.get(0)
            val secondDevice = compareHeadItems?.get(1)
            val firstDeviceTitle = compareHead?.get(1)?.select("div.compare-head-item")?.get(0)
                ?.select("a>div#first-phone")?.text() ?: ""
            val secondDeviceTitle = compareHead?.get(1)?.select("div.compare-head-item")?.get(1)
                ?.select("a>div#second-phone")?.text() ?: ""

            val first = firstDevice?.select("div.compare-head-main-score")?.text() ?: ""
            val second = secondDevice?.select("div.compare-head-main-score")?.text() ?: ""

            val firstImgUrl = firstDevice?.select("img")?.attr("src") ?: ""
            val secondImgUrl = secondDevice?.select("img")?.attr("src") ?: ""

            return CompareScore(
                first,
                second,
                firstImgUrl,
                secondImgUrl,
                firstDeviceTitle,
                secondDeviceTitle
            )
        } catch (e: Exception) {
            return null
        }
    }

    private fun getDataFromCompareTable(table: Element?): List<CompareScoreRow?> {
        val specRows = table?.select("tbody>tr")
        val compareScoreRows = mutableListOf<CompareScoreRow?>()

        specRows?.forEach { specRow ->
            val specName = specRow.select("td.cell-h").text()
            val first = specRow.select("td.cell-s1").text()
            val second = specRow.select("td.cell-s2").text()

            compareScoreRows.add(CompareScoreRow(specName, first, second))
        }

        return compareScoreRows
    }

    private fun getDataFromCompareScoreBars(twoColumns: Element?): List<CompareScoreBar?> {
        val twoColumnItems = twoColumns?.select("div.two-columns-item")
        val scoreBarList = mutableListOf<CompareScoreBar?>()

        try {
            twoColumnItems?.forEach { twoColumnsItem ->
                val scoreBar = extractScoreData(twoColumnsItem.select("div>div").first())
                if (scoreBar?.name!!.isNotEmpty()) scoreBarList.add(scoreBar)
            }
        } catch (e: Exception) {
        }

        return scoreBarList
    }

    private fun extractScoreData(twoColumnsItem: Element?): CompareScoreBar? {
        val title = twoColumnsItem?.select("div>div.title-h4")?.text() ?: ""
        val scoreElements = twoColumnsItem?.select("div>div.mb")

        val firstScoreResult =
            scoreElements?.get(0)?.select("div.score-bar>div.score-bar-result>span")?.first()
                ?.text() ?: ""
        val firstScoreValue = scoreElements?.get(0)
            ?.select("div.score-bar>div.score-bar-line>div.score-bar-line-filled")?.attr("style")
            ?.replace("width:", "")?.trim() ?: ""

        val secondScoreResult =
            scoreElements?.get(1)?.select("div.score-bar>div.score-bar-result>span")?.text() ?: ""
        val secondScoreValue = scoreElements?.get(1)
            ?.select("div.score-bar>div.score-bar-line>div.score-bar-line-filled")?.attr("style")
            ?.replace("width:", "")?.trim() ?: ""

        return CompareScoreBar(
            title,
            firstScoreResult,
            firstScoreValue,
            secondScoreResult,
            secondScoreValue
        )

    }

    private fun getKeyDifferences(card: Element): CompareKeyDifferences? {
        try {
            val cardBlocks = card.select("div.card-block")
            val title = cardBlocks.get(0).select("div.card-head").text()

            val titles = cardBlocks.get(1).select("div.title-h4")
            val pros = cardBlocks.get(1).select("ul.proscons-list")

            val firstTitle = titles.get(0).text()
            val firstPros = pros.get(0).children().toList().map { it.text() }


            val secondTitle = titles.get(1).text()
            val secondPros = pros.get(1).children().toList().map { it.text() }

            return CompareKeyDifferences(
                title = title,
                firstKeyDifference = KeyDifference(firstTitle, firstPros),
                secondDifference = KeyDifference(secondTitle, secondPros)
            )
        } catch (e: Exception) {
            return null
        }
    }
}

fun main() {
    println(
        CompareApi.compareDevices(
            "Acer Extensa 15",
            "acer Aspire 3 Spin 14",
            ProductType.Laptop
        )
    )
}
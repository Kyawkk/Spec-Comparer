package com.kyawzinlinn.speccomparer.network.api

import com.kyawzinlinn.speccomparer.domain.model.detail.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.detail.HeaderData
import com.kyawzinlinn.speccomparer.domain.model.detail.KeyDifference
import com.kyawzinlinn.speccomparer.domain.model.detail.ProductDetail
import com.kyawzinlinn.speccomparer.domain.model.detail.ProductSpecification
import com.kyawzinlinn.speccomparer.domain.model.detail.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.model.detail.SpecificationColumn
import com.kyawzinlinn.speccomparer.domain.model.detail.SpecificationItem
import com.kyawzinlinn.speccomparer.domain.model.detail.SpecificationTable
import com.kyawzinlinn.speccomparer.domain.utils.CssParser
import com.kyawzinlinn.speccomparer.domain.utils.JsoupConfig
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.getAllChildren
import com.kyawzinlinn.speccomparer.domain.utils.toPath
import org.jsoup.nodes.Element
import java.util.Locale

object DetailApi {
    private const val TAG = "DetailApi"
    fun getProductSpecification(
        path: String,
        type: ProductType
    ): ProductSpecificationResponse {
        val document = JsoupConfig.connectProductDetailsWebUrl(
            path,
            type
        )

        val cardElements = document.select("body>div.main-container>main>article>div.card")
        val productDetails = getProductDetails(cardElements.get(0))
        val specifications = mutableListOf<SpecificationItem>()

        cardElements.forEach {
            val specificationItem = extractDataFromCard(it)
            if (specificationItem.specificationsColumn.isNotEmpty() || specificationItem.specificationsTable.isNotEmpty()) {
                specifications.add(specificationItem)
            }
        }
        return ProductSpecificationResponse(productDetails, specifications)
    }

    private fun getProductDetails(element: Element): ProductSpecification {
        val productName = element.select("div.card-block>div.card-head>h1").text()
        val productImageUrl = element.select("div.card-block>div>img").attr("src")
        val specElements = element.select("div.card-block>ul>li")
        val productSpecifications = mutableListOf<ProductDetail>()
        specElements.forEach {
            val segments = it.text().split(":")
            productSpecifications.add(
                ProductDetail(
                    segments.getOrNull(0) ?: "",
                    segments.getOrNull(1) ?: "",
                )
            )
        }

        return ProductSpecification(productName, productImageUrl, productSpecifications)
    }

    private fun extractDataFromCard(element: Element?): SpecificationItem {
        val specificationColumns = mutableListOf<SpecificationColumn>()
        val specificationTable = mutableListOf<SpecificationTable>()

        val cardBlocks = element?.select("div.card-block")
        val title = if (cardBlocks?.size != 0) cardBlocks?.get(0)?.select("div.card-head")?.text() ?: "" else ""
        if (cardBlocks?.size != 0) cardBlocks?.remove(cardBlocks?.get(0))
        val tables = element?.select("table")

        if (tables!!.isNotEmpty()) {
            tables.forEach { table ->
                extractDataFromTable(table).forEach {
                    specificationTable.add(it)
                }
            }
        }

        cardBlocks?.forEach {
            specificationColumns.addAll(extractData(it))
        }

        return SpecificationItem(title, specificationColumns, specificationTable)
    }

    private fun extractData(cardBlock: Element): List<SpecificationColumn> {
        val columnBlock = cardBlock.select("div.two-columns")
        val specifications = mutableListOf<SpecificationColumn>()

        if (columnBlock.isNotEmpty()) {
            columnBlock.select("div.two-columns-item").forEach {
                specifications.add(extractDataFromColumn(it))
            }
        }
        return specifications
    }

    private fun extractDataFromTable(table: Element): List<SpecificationTable> {
        val specifications = mutableListOf<SpecificationTable>()
        val rows = table.select("tbody>tr")
        rows.forEach {
            val title = it.select("td.cell-h").text()
            val first = it.select("td.cell-s").text()
            specifications.add(SpecificationTable(title, first))
        }

        return specifications
    }

    private fun extractDataFromColumn(columnItem: Element): SpecificationColumn {
        val scoreBar = columnItem.select("div.score-bar")
        val title = scoreBar.select("div.score-bar-name").text() ?: ""

        val firstStyle =
            scoreBar.select("div.score-bar-line>div")?.attr("style")
                ?.replace("%", "") ?: ""

        val progress = CssParser.parse(firstStyle, "width") ?: ""

        val value = scoreBar.select("div.score-bar-result").text() ?: ""
        return SpecificationColumn(title, value, progress)
    }
}
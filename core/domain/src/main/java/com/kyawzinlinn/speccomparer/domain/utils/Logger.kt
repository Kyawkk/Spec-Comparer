package com.kyawzinlinn.speccomparer.domain.utils

import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationColumn
import org.jsoup.select.Elements

fun <T : Any> List<T>.printAll(){
    this.forEach {
        println(it)
    }
}

fun Elements.printAll(){
    this.forEach { println(it.text()) }
}

const val TOTAL_WIDTH = 101

fun CompareResponse.print(){
    val header = "*"+" ".repeat(20) + headerData.firstDevice + " ".repeat(5) + "|" + " ".repeat(5) + this.headerData.secondDevice + " ".repeat(20) + "*"
    val keyDifference = "Key Differences"
    val halfSpaces = (TOTAL_WIDTH - keyDifference.length) / 2
    val keyDifferenceTitle = "*" + " ".repeat(halfSpaces - 1) + keyDifference + " ".repeat(halfSpaces - 1) + "*"
    val headline = "*" + " ".repeat(halfSpaces - 1) + "-".repeat(keyDifference.length) + " ".repeat(halfSpaces - 1) + "*"

    printFullLine()
    println(header)
    printFullLine()
    println(keyDifferenceTitle)
    println(headline)


    keyDifferences.forEach {
        println("1. ${it.title}".leftAlign(3))
        printLine()
        it.facts.forEach {
            println(it.leftAlign(5))
        }
        printLine()
        printLine()
    }

    compares.forEach {
        println(it.title.alignCenter())
        printLine()
        it.specificationsColumn.forEach {
            printColumnData(it)
        }
    }
}

private fun printLine () {
    println("*" + " ".repeat(TOTAL_WIDTH - 2) + "*")
}

private fun printFullLine(){
    println("*".repeat(TOTAL_WIDTH))
}

private fun String.leftAlign(spacing: Int) : String {
    val spaces = TOTAL_WIDTH - (this.length + spacing)
    return "*" + " ".repeat(spacing)+ this + " ".repeat(spaces - 2) + "*"
}

private fun String.printProgress() {
    val total = 10
    val progress = (this.toInt()/10).toInt()
    println("["+"#".repeat(progress) + "-".repeat(total-progress) + "]")
}

private fun printColumnData(specificationColumn: SpecificationColumn) {
    val title = specificationColumn.name.trim().alignCenter()
    println(title +  " ${title.length}")
    specificationColumn.progress.printProgress()
}

private fun String.alignCenter() : String {
    val halfSpaces = (TOTAL_WIDTH - this.length) / 2
    return "*" + " ".repeat(halfSpaces - 1) + this + " ".repeat(halfSpaces - 1) + "*"
}

fun ProductSpecificationResponse.print() {
    println(this.productSpecification)
    this.productSpecifications.forEach {
        println(it)
    }
}
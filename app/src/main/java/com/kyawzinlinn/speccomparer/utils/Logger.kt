package com.kyawzinlinn.speccomparer.utils

import org.jsoup.select.Elements

fun <T : Any> List<T>.printAll(){
    this.forEach {
        println(it)
    }
}

fun Elements.printAll(){
    this.forEach { println(it.text()) }
}
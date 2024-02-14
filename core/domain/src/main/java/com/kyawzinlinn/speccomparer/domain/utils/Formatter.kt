package com.kyawzinlinn.speccomparer.domain.utils

fun String.removeSpecialCharacters(): String {
    val regex = Regex("[^0-9]")
    return this.replace(regex,"")
}
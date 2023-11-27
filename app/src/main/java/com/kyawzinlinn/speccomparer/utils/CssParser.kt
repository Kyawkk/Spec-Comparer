package com.kyawzinlinn.speccomparer.utils

import org.htmlunit.cssparser.parser.CSSOMParser

object CssParser {
    fun parse(style: String, attribute: String) : String {
        val parser = CSSOMParser()
        val declaredStyle = parser.parseStyleDeclaration(style)
        return declaredStyle.getPropertyValue(attribute)
    }
}
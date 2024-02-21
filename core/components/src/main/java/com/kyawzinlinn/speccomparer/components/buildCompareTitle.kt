package com.kyawzinlinn.speccomparer.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun buildCompareTitle (title: String): AnnotatedString {
    val selectedStyle = SpanStyle(
        background = MaterialTheme.colorScheme.primary,
        color = Color.White
    )
    return try {
        buildAnnotatedString {
            withStyle(style = selectedStyle) {
                append(" ${title.substring(0, 2)} ")
            }
            append(title.substring(2))
        }
    } catch (e: Exception) {
        buildAnnotatedString { title }
    }
}

@Preview (showBackground = true)
@Composable
private fun CompareTitlePreview() {
    Text(
        modifier = Modifier.padding(8.dp),
        text = buildCompareTitle(title = "50 out of 100")
    )
}
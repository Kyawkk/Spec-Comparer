package com.kyawzinlinn.speccomparer.compare.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CompareItem(
    firstContent: @Composable () -> Unit,
    secondContent: @Composable () -> Unit
) {
    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) { firstContent() }
        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) { secondContent() }
    }
}
package com.kyawzinlinn.speccomparer.design_system.extensions

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.dividerColor: Color @Composable
        get() = getDividerColor()

@Composable
fun getDividerColor(): Color {
    return MaterialTheme.colorScheme.onBackground.copy(0.3f)
}
package com.kyawzinlinn.speccomparer.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kyawzinlinn.speccomparer.design_system.R

// Set of Material typography styles to start with
val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_thin, FontWeight.Thin),
    Font(R.font.inter_black, FontWeight.Black),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight),
)

val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = Inter),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = Inter),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = Inter),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = Inter),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = Inter),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = Inter),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = Inter),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = Inter),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = Inter),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Inter),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = Inter),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = Inter),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = Inter),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = Inter),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = Inter)
)
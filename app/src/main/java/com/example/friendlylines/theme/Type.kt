package com.example.friendlylines.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object FriendlyLinesTextStyles {
    val displayD1: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 71.66.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val displayD2: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 59.72.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH1Regular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 49.77.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH1Medium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 49.77.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH2Regular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 41.47.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH2Medium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 41.47.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH3Regular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 34.56.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH3Medium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 34.56.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH4Regular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 28.80.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH4Medium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 28.80.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH5Regular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.00.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val headingH5Medium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.00.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val bodyRegular: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.00.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val bodyMedium: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.00.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val button: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.00.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val captionC1: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.67.sp.scaled(),
                lineHeight = 1.5.em,
            )
    val captionC2: TextStyle
        @Composable get() =
            TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.89.sp.scaled(),
                lineHeight = 1.5.em,
            )
}

@Composable
fun friendlyLinesTypography(): Typography =
    Typography(
        displayLarge = FriendlyLinesTextStyles.displayD1,
        displayMedium = FriendlyLinesTextStyles.displayD2,
        displaySmall = FriendlyLinesTextStyles.headingH1Regular,
        headlineLarge = FriendlyLinesTextStyles.headingH2Regular,
        headlineMedium = FriendlyLinesTextStyles.headingH2Regular,
        headlineSmall = FriendlyLinesTextStyles.headingH3Regular,
        titleLarge = FriendlyLinesTextStyles.headingH3Medium,
        titleMedium = FriendlyLinesTextStyles.headingH4Regular,
        titleSmall = FriendlyLinesTextStyles.headingH4Medium,
        bodyLarge = FriendlyLinesTextStyles.headingH5Regular,
        bodyMedium = FriendlyLinesTextStyles.bodyRegular,
        bodySmall = FriendlyLinesTextStyles.captionC1,
        labelLarge = FriendlyLinesTextStyles.button,
        labelMedium = FriendlyLinesTextStyles.captionC1,
        labelSmall = FriendlyLinesTextStyles.captionC2,
    )

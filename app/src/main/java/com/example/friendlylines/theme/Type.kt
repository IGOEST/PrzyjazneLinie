package com.example.friendlylines.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R


/**
 * Typography
 * Use ex. Text("text", style = AppTextStyles.h3Medium)
 */

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val rubik = GoogleFont("Rubik")

private val RubikFontFamily = FontFamily(
    Font(googleFont = rubik, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = rubik, fontProvider = googleFontProvider, weight = FontWeight.Medium)
)


val Typography = Typography(
    displayLarge = TextStyle( // D1
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 72.sp,
        lineHeight = 85.sp
    ),
    displayMedium = TextStyle( // D2
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 59.7.sp,
        lineHeight = 89.55.sp
    ),
    headlineLarge = TextStyle( // H1
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 49.8.sp,
        lineHeight = 74.7.sp
    ),
    headlineMedium = TextStyle( // H2
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 41.5.sp,
        lineHeight = 62.25.sp
    ),
    headlineSmall = TextStyle( // H3, Regular
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.6.sp,
        lineHeight = 51.9.sp
    ),
    titleLarge = TextStyle( // H4, Regular
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.8.sp,
        lineHeight = 43.2.sp
    ),
    titleMedium = TextStyle( // H5, Regular
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 36.sp
    ),
    titleSmall = TextStyle( // BUTTON
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),
    bodyLarge = TextStyle( // Body, Regular
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 30.sp
    ),
    bodySmall = TextStyle( // C1 Caption
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.7.sp,
        lineHeight = 25.05.sp
    ),
    labelSmall = TextStyle( // C2 Caption
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.9.sp,
        lineHeight = 20.85.sp
    ),
    labelLarge = TextStyle( // Button
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 30.sp
    )
)

object AppTextStyles {
    val d1 = Typography.displayLarge
    val d2 = Typography.displayMedium

    val h1 = Typography.headlineLarge
    val h2 = Typography.headlineMedium

    val h3 = Typography.headlineSmall
    val h3Medium = h3.copy(fontWeight = FontWeight.Medium)

    val h4 = Typography.titleLarge
    val h4Medium = h4.copy(fontWeight = FontWeight.Medium)

    val h5 = Typography.titleMedium
    val h5Medium = h5.copy(fontWeight = FontWeight.Medium)

    val bodyRegular = Typography.bodyLarge
    val bodyMedium = bodyRegular.copy(fontWeight = FontWeight.Medium)

    val button = Typography.titleSmall

    val c1Caption = Typography.bodySmall
    val c2Caption = Typography.labelSmall
}

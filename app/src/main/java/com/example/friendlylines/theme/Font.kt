package com.example.friendlylines.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
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

val RubikFontFamily = FontFamily(
    Font(googleFont = rubik, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = rubik, fontProvider = googleFontProvider, weight = FontWeight.Medium)
)
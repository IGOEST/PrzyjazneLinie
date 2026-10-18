package com.example.friendlylines.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FriendlyLinesColorScheme =
    lightColorScheme(
        primary = FriendlyLinesColors.PrimaryFriendlyLines.P700,
        onPrimary = FriendlyLinesColors.Shades.White,
        secondary = FriendlyLinesColors.PrimaryFriendlyLines.P300,
        onSecondary = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
        tertiary = FriendlyLinesColors.Shades.White,
        onTertiary = FriendlyLinesColors.PrimaryFriendlyLines.P700,
        primaryContainer = FriendlyLinesColors.PrimaryFriendlyLines.P300,
        onPrimaryContainer = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
        background = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        onBackground = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
        surface = FriendlyLinesColors.PrimaryFriendlyLines.P300,
        onSurface = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
        error = FriendlyLinesColors.States.Error700,
        onError = FriendlyLinesColors.Shades.White,
        errorContainer = FriendlyLinesColors.States.Error900,
    )

@Composable
fun FriendlyLinesTheme(content: @Composable () -> Unit) {
    ProvideAdaptiveScale {
        MaterialTheme(
            colorScheme = FriendlyLinesColorScheme,
            typography = friendlyLinesTypography(),
            shapes = FriendlyLinesShapes,
            content = content,
        )
    }
}

package com.example.friendlylines.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private const val REFERENCE_WIDTH_DP = 1280f
private const val REFERENCE_HEIGHT_DP = 800f
private const val MIN_SCALE = 0.7f
private const val MAX_SCALE = 1.15f

private fun axisScale(currentDp: Float, referenceDp: Float, ): Float = (currentDp / referenceDp).coerceIn(MIN_SCALE, MAX_SCALE)

fun computeAdaptiveScale(currentWidthDp: Float, referenceWidthDp: Float = REFERENCE_WIDTH_DP, ): Float = axisScale(currentWidthDp, referenceWidthDp)

val LocalAdaptiveScale = staticCompositionLocalOf { 1f }

@Composable
fun ProvideAdaptiveScale(content: @Composable () -> Unit) {
    val widthDp = LocalConfiguration.current.screenWidthDp.toFloat()
    CompositionLocalProvider(LocalAdaptiveScale provides computeAdaptiveScale(widthDp)) {
        content()
    }
}

@Composable
fun Dp.scaled(): Dp = this * LocalAdaptiveScale.current

@Composable
fun TextUnit.scaled(): TextUnit = (this.value * LocalAdaptiveScale.current).sp

@Composable
fun rememberMinAxisScale(): Float {
    val configuration = LocalConfiguration.current
    val widthScale = axisScale(configuration.screenWidthDp.toFloat(), REFERENCE_WIDTH_DP)
    val heightScale = axisScale(configuration.screenHeightDp.toFloat(), REFERENCE_HEIGHT_DP)
    return minOf(widthScale, heightScale)
}

package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.SliderBG
import com.example.friendlylines.theme.scaled
import kotlin.math.roundToInt

val SLIDER_WIDTH = 350.dp
private val LABELS_SPACING = 3.dp
private val HEADER_SPACING = 4.dp
private val ICONS_SPACING = 12.dp
private val ICONS_SIZE = 24.dp
private val SLIDER_HEIGHT = 25.dp
private val SLIDER_HORIZONTAL_PADDING = 3.dp

@Composable
fun <T> TemplateSlider(
    values: List<T>,
    selectedIndex: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,

    value: Int? = null,
    minValue: Int? = null,
    maxValue: Int? = null,

    sliderWidth: Dp? = null,

    enabled: Boolean = true,

    showHeader: Boolean = false,
    showInfoIcon: Boolean = false,
    infoTitle: String? = null,
    infoMessage: String? = null,
    headerText: String? = null,

    showMinusPlus: Boolean = false,
    onMinusClick: (() -> Unit)? = null,
    onPlusClick: (() -> Unit)? = null,

    activeColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
    inactiveColor: Color = SliderBG,
    labelColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
    headerTitleColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,

    label: (T) -> String = { it.toString() }
) {
    val labelSpacing = LABELS_SPACING.scaled()
    val headerSpacing = HEADER_SPACING.scaled()
    val minusPlusSpacing = ICONS_SPACING.scaled()

    val actualIndex = selectedIndex.coerceIn(0, values.lastIndex.coerceAtLeast(0))

    val sliderValue = if (
        value != null &&
        minValue != null &&
        maxValue != null &&
        maxValue != minValue
    ) {
        value.toFloat()
    } else {
        actualIndex.toFloat()
    }

    val sliderMin = if (
        value != null &&
        minValue != null &&
        maxValue != null &&
        maxValue != minValue
    ) {
        minValue.toFloat()
    } else {
        0f
    }

    val sliderMax = if (
        value != null &&
        minValue != null &&
        maxValue != null &&
        maxValue != minValue
    ) {
        maxValue.toFloat()
    } else {
        values.lastIndex.coerceAtLeast(0).toFloat()
    }

    Column(
        modifier = modifier.wrapContentWidth()
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier
                    .then(
                        if(showMinusPlus) {
                            Modifier
                                .padding(
                                    start = minusPlusSpacing + 24.dp.scaled()
                                )
                        } else {
                            Modifier
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showInfoIcon) {
                    InfoIconButton(
                        infoTitle = infoTitle,
                        infoMessage = infoMessage,
                        modifier = Modifier.size(ICONS_SIZE.scaled()),
                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                        disabledTint = FriendlyLinesColors.Neutral.N300
                    )

                    Spacer(modifier = Modifier.width(16.dp.scaled()))
                }

                if (headerText != null) {
                    Text(
                        text = headerText,
                        color = headerTitleColor,
                        style = FriendlyLinesTextStyles.bodyRegular
                    )
                }
            }

            Spacer(modifier = Modifier.height(headerSpacing))
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.Top
        ) {
            if (showMinusPlus) {
                TemplateClickableIcon(
                    icon = Icons.Filled.IndeterminateCheckBox,
                    contentDescription = null,
                    onClick = {
                        onMinusClick?.invoke()
                    },
                    enabled = enabled && when {
                        value != null && minValue != null -> value > minValue
                        else -> actualIndex > 0
                    },
                    modifier = Modifier.size(ICONS_SIZE.scaled()),
                    defaultTint = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                    activeTint = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                    disabledTint = FriendlyLinesColors.PrimaryFriendlyLines.P300
                )

                Spacer(modifier = Modifier.width(minusPlusSpacing))
            }

            Column(
                modifier = Modifier
                    .then(
                        if(sliderWidth != null) {
                            Modifier.width(sliderWidth.scaled())
                        } else {
                            Modifier.weight(1f)
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(labelSpacing)
            ) {
                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        if (
                            value != null &&
                            minValue != null &&
                            maxValue != null &&
                            maxValue != minValue
                        ) {
                            val normalized = ((newValue - minValue).toFloat() / (maxValue - minValue)).coerceIn(0f, 1f)

                            val index = (normalized * values.lastIndex)
                                .roundToInt()
                                .coerceIn(0, values.lastIndex)

                            onValueSelected(index)
                        } else {
                            val index = newValue
                                .roundToInt()
                                .coerceIn(0, values.lastIndex)

                            onValueSelected(index)
                        }
                    },
                    valueRange = sliderMin..sliderMax,
                    steps = if (sliderMax > sliderMin) {
                        (sliderMax - sliderMin)
                            .roundToInt()
                            .minus(1)
                            .coerceAtLeast(0)
                    } else { 0 },
                    enabled = enabled && values.isNotEmpty(),
                    colors = SliderDefaults.colors(
                        thumbColor = if (enabled) {
                            activeColor
                        } else {
                            FriendlyLinesColors.PrimaryFriendlyLines.P300
                        },
                        activeTrackColor = if (enabled) {
                            activeColor.copy(alpha = 0.35f)
                        } else {
                            FriendlyLinesColors.Neutral.N300
                        },
                        inactiveTrackColor = if (enabled) {
                            inactiveColor
                        } else {
                            FriendlyLinesColors.Neutral.N300
                        },
                        disabledThumbColor = FriendlyLinesColors.PrimaryFriendlyLines.P300,
                        disabledActiveTrackColor = FriendlyLinesColors.Neutral.N300,
                        disabledInactiveTrackColor = FriendlyLinesColors.Neutral.N300
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SLIDER_HEIGHT.scaled())
                        .padding(horizontal = SLIDER_HORIZONTAL_PADDING.scaled())
                )

                if (values.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = if (values.size >= 10) {
                                    0.dp
                                } else {
                                    8.dp
                                }
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        values.forEach { item ->
                            Text(
                                text = label(item),
                                color = labelColor,
                                style = FriendlyLinesTextStyles.captionC1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (showMinusPlus) {
                Spacer(modifier = Modifier.width(minusPlusSpacing))

                TemplateClickableIcon(
                    icon = Icons.Filled.AddBox,
                    contentDescription = null,
                    onClick = {
                        onPlusClick?.invoke()
                    },
                    enabled = enabled && when {
                        value != null && maxValue != null -> value < maxValue
                        else -> actualIndex < values.lastIndex
                    },
                    modifier = Modifier.size(ICONS_SIZE.scaled()),
                    defaultTint = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                    activeTint = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                    disabledTint = FriendlyLinesColors.PrimaryFriendlyLines.P300
                )
            }
        }
    }
}

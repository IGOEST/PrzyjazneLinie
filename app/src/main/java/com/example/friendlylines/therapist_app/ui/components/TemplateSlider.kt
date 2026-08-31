package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.friendlylines.therapist_app.ui.theme.SliderBG
import com.example.friendlylines.therapist_app.ui.theme.SliderGlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun <T> TemplateSlider(
    values: List<T>,
    selectedIndex: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,

    sliderWidth: Dp = 350.dp,
    trackHeight: Dp = 6.dp,
    thumbRadius: Dp = 10.dp,

    showHeader: Boolean = false,
    headerText: String? = null,
    headerIcon: ImageVector? = null,
    headerIconContentDescription: String? = null,
    onHeaderIconClick: (() -> Unit)? = null,

    showMinusPlus: Boolean = false,
    onMinusClick: (() -> Unit)? = null,
    onPlusClick: (() -> Unit)? = null,

    activeColor: Color = Primary700,
    activeEffectColor: Color = SliderGlow,
    inactiveColor: Color = SliderBG,
    labelColor: Color = Primary900,
    headerTitleColor: Color = Primary1000,

    label: (T) -> String = { it.toString() }
) {
    val labelSpacing = 4.dp
    val headerSpacing = 4.dp
    val minusPlusSpacing = 12.dp

    val density = LocalDensity.current

    var isActive by remember {
        mutableStateOf(false)
    }

    val interactionScope = rememberCoroutineScope()

    fun activateEffect() {
        interactionScope.launch {
            isActive = true
            delay(250.milliseconds)
            isActive = false
        }
    }

    val actualIndex = selectedIndex.coerceIn(0, values.lastIndex)

    Column(
        modifier = modifier.wrapContentWidth()
    ) {
        if (showHeader) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (headerIcon != null && onHeaderIconClick != null
                ) {
                    TemplateClickableIcon(
                        icon = headerIcon,
                        contentDescription = headerIconContentDescription,
                        onClick = onHeaderIconClick,
                        modifier = Modifier.size(24.dp),
                        defaultTint = InfoDefault,
                        activeTint = InfoActive
                    )
                }

                if (headerText != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = headerText,
                        color = headerTitleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(headerSpacing))
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showMinusPlus) {
                TemplateClickableIcon(
                    icon = Icons.Default.IndeterminateCheckBox,
                    contentDescription = "Zmniejsz wartość",
                    onClick = {
                        onMinusClick?.invoke()
                        activateEffect()
                    },
                    enabled = actualIndex > 0,
                    modifier = Modifier.size(24.dp),
                    defaultTint = Primary700,
                    activeTint = Primary900,
                    disabledTint = Primary300
                )

                Spacer(modifier = Modifier.width(minusPlusSpacing))
            }

            Canvas(
                modifier = Modifier
                    .width(sliderWidth)
                    .height(40.dp)
                    .pointerInput(values) {
                        detectTapGestures { offset ->
                            val width = size.width.toFloat()
                            val position = (offset.x / width).coerceIn(0f, 1f)
                            val index =
                                if (values.size == 1) {
                                    0
                                } else {
                                    (position * values.lastIndex).roundToInt()
                                }
                            onValueSelected(index)
                            activateEffect()
                        }
                    }
                    .pointerInput(values) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                isActive = true
                            },
                            onDragEnd = {
                                isActive = false
                            },
                            onDragCancel = {
                                isActive = false
                            }
                        ) { change, _ ->
                            val width = size.width.toFloat()
                            val position = (change.position.x / width).coerceIn(0f, 1f)
                            val index =
                                if (values.size == 1) {
                                    0
                                } else {
                                    (position * values.lastIndex).roundToInt()
                                }
                            onValueSelected(index)
                            change.consume()
                        }
                    }
            ) {
                if (values.size == 1) {
                    return@Canvas
                }
                val trackY = size.height / 2f
                val startX = 0f
                val endX = size.width
                val selectedPosition = actualIndex.toFloat() / values.lastIndex
                val thumbX = startX + (endX - startX) * selectedPosition

                // Drag effect
                if (isActive) {
                    drawCircle(
                        color = activeEffectColor,
                        radius = with(density) {
                            (thumbRadius + 7.dp).toPx()
                        },
                        center = Offset(thumbX, trackY)
                    )
                }

                // Background
                drawLine(
                    color = inactiveColor,
                    start = Offset(startX, trackY),
                    end = Offset(endX, trackY),
                    strokeWidth = with(density) {
                        trackHeight.toPx()
                    },
                    cap = StrokeCap.Round
                )

                // Active bar
                drawLine(
                    color = activeColor,
                    start = Offset(startX, trackY),
                    end = Offset(thumbX, trackY),
                    strokeWidth = with(density) {
                        trackHeight.toPx()
                    },
                    cap = StrokeCap.Round
                )

                // Dot
                drawCircle(
                    color = activeColor,
                    radius = with(density) {
                        thumbRadius.toPx()
                    },
                    center = Offset(thumbX, trackY)
                )
            }

            if (showMinusPlus) {
                Spacer(modifier = Modifier.width(minusPlusSpacing))

                TemplateClickableIcon(
                    icon = Icons.Default.AddBox,
                    contentDescription = "Zwiększ wartość",
                    onClick = {
                        onPlusClick?.invoke()
                        activateEffect()
                    },
                    enabled = actualIndex < values.lastIndex,
                    modifier = Modifier.size(24.dp),
                    defaultTint = Primary700,
                    activeTint = Primary900,
                    disabledTint = Primary300
                )
            }
        }

        Spacer(modifier = Modifier.height(labelSpacing))

        // Labels
        Row(
            modifier = Modifier
                .width(sliderWidth)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            values.forEach { value ->
                Text(
                    text = label(value),
                    color = labelColor,
                    fontSize = 16.7.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
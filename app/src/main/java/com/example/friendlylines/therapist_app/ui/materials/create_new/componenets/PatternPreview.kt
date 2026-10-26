package com.example.friendlylines.therapist_app.ui.materials.create_new.componenets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.materials.geometry.NormalizedBounds
import com.example.friendlylines.therapist_app.ui.materials.geometry.calculateNormalizedBounds
import com.example.shared.data.models.PatternDrawing
import com.example.shared.data.models.Point
import kotlin.random.Random

private val STROKE_WIDTH = 5.dp
private val SINGLE_POINT_RADIUS = 4.dp

private data class PatternTransform(
    val scale: Float,
    val rotation: Float
)

@Composable
fun PatternPreview(
    drawing: PatternDrawing,
    smoothingEnabled: Boolean,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = STROKE_WIDTH.scaled(),
    strokeColor: Color = FriendlyLinesColors.Shades.Black,
    singlePointRadius: Dp = SINGLE_POINT_RADIUS.scaled(),
    showStartingPoint: Boolean = false,
    startingPointColor: Color = Color(0xFF00D97E),//FriendlyLinesColors.Shades.White,
    patternVariety: Boolean = false,
    varietyKey: Int = 0
) {
    val transform = remember(drawing, patternVariety, varietyKey) {
        if (patternVariety) {
            PatternTransform(
                scale = Random.nextFloat() * 0.4f + 0.8f,
                rotation = Random.nextFloat() * 40f - 20f
            )
        } else {
            PatternTransform(
                scale = 1f,
                rotation = 0f
            )
        }
    }

    Canvas(
        modifier = modifier
    ) {
        val bounds = calculateNormalizedBounds(
            strokes = drawing.strokes
        ) ?: return@Canvas

        if (bounds.width < 0f || bounds.height < 0f) {
            return@Canvas
        }

        val padding = 10.dp.toPx()

        val availableWidth = (size.width - 2f * padding).coerceAtLeast(1f)
        val availableHeight = (size.height - 2f * padding).coerceAtLeast(1f)

        val radians = Math.toRadians(
            transform.rotation.toDouble()
        )

        val cos = kotlin.math.abs(kotlin.math.cos(radians)).toFloat()
        val sin = kotlin.math.abs(kotlin.math.sin(radians)).toFloat()

        val effectiveWidth = bounds.width.coerceAtLeast(0.001f)
        val effectiveHeight = bounds.height.coerceAtLeast(0.001f)

        val rotatedWidth = effectiveWidth * cos + effectiveHeight * sin
        val rotatedHeight = effectiveWidth * sin + effectiveHeight * cos

        val safeRotatedWidth = rotatedWidth.coerceAtLeast(0.001f)
        val safeRotatedHeight = rotatedHeight.coerceAtLeast(0.001f)

        val baseScale = minOf(
            availableWidth / safeRotatedWidth,
            availableHeight / safeRotatedHeight
        )

        val scale = baseScale * transform.scale


        val drawingWidth = bounds.width * scale
        val drawingHeight = bounds.height * scale

        val offset = Offset(
            x = (size.width - drawingWidth) / 2f,
            y = (size.height - drawingHeight) / 2f
        )

        rotate(
            degrees = transform.rotation,
            pivot = center
        ) {
            drawing.strokes.forEach { stroke ->
                drawNormalizedStroke(
                    points = stroke,
                    bounds = bounds,
                    scale = scale,
                    offset = offset,
                    smoothingEnabled = smoothingEnabled,
                    strokeWidth = strokeWidth,
                    strokeColor = strokeColor,
                    singlePointRadius = singlePointRadius
                )
            }

            if (showStartingPoint) {
                val firstPoint = drawing.strokes
                    .firstOrNull()
                    ?.firstOrNull()

                if (firstPoint != null) {
                    val startPosition = Offset(
                        x = (firstPoint.x - bounds.minX) * scale + offset.x,
                        y = (firstPoint.y - bounds.minY) * scale + offset.y
                    )

                    val startRadius = (strokeWidth * 1.1f).toPx()

                    drawCircle(
                        color = Color(0xFF263238),
                        radius = startRadius * 0.9f,
                        center = startPosition
                    )

                    drawCircle(
                        color = Color.White,
                        radius = startRadius * 0.7f,
                        center = startPosition
                    )

                    drawCircle(
                        color = startingPointColor,
                        radius = startRadius * 0.5f,
                        center = startPosition
                    )

                    drawCircle(
                        color = Color.White,
                        radius = startRadius * 0.2f,
                        center = startPosition
                    )
                }
            }
        }
    }
}

fun DrawScope.drawNormalizedStroke(
    points: List<Point>,
    bounds: NormalizedBounds,
    scale: Float,
    offset: Offset,
    smoothingEnabled: Boolean,
    strokeWidth: Dp,
    strokeColor: Color,
    singlePointRadius: Dp
) {
    if (points.isEmpty()) {
        return
    }

    val drawingPoints = points.map { point ->
        Offset(
            x = (point.x - bounds.minX) * scale + offset.x,
            y = (point.y - bounds.minY) * scale + offset.y
        )
    }

    drawPoints(
        points = drawingPoints,
        smoothingEnabled = smoothingEnabled,
        strokeWidth = strokeWidth.toPx(),
        singlePointRadius = singlePointRadius.toPx(),
        strokeColor = strokeColor
    )
}
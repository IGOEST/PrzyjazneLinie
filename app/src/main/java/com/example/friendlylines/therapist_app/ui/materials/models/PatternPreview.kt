package com.example.friendlylines.therapist_app.ui.materials.models

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.therapist_app.ui.materials.geometry.NormalizedBounds
import com.example.friendlylines.therapist_app.ui.materials.geometry.calculateNormalizedBounds
import com.example.shared.data.models.Point

@Composable
fun PatternPreview(
    drawing: PatternDrawing,
    smoothingEnabled: Boolean,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    strokeColor: Color = Color.Black,
    singlePointRadius: Dp = 4.dp
) {
    Canvas(
        modifier = modifier
    ) {
        val bounds = calculateNormalizedBounds(
            strokes = drawing.strokes
        ) ?: return@Canvas

        if (bounds.width <= 0f || bounds.height <= 0f) {
            return@Canvas
        }

        val padding = 10.dp.toPx()

        val availableWidth = size.width - 2f * padding
        val availableHeight = size.height - 2f * padding

        val scale = minOf(
            availableWidth / bounds.width,
            availableHeight / bounds.height
        )

        val drawingWidth = bounds.width * scale
        val drawingHeight = bounds.height * scale

        val offsetX = (size.width - drawingWidth) / 2f

        val offsetY = (size.height - drawingHeight) / 2f

        drawing.strokes.forEach { stroke ->
            drawNormalizedStroke(
                points = stroke,
                bounds = bounds,
                scale = scale,
                offset = Offset(
                    x = offsetX,
                    y = offsetY
                ),
                smoothingEnabled = smoothingEnabled,
                strokeWidth = strokeWidth,
                strokeColor = strokeColor,
                singlePointRadius = singlePointRadius
            )
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
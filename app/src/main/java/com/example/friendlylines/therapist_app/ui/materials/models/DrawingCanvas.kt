package com.example.friendlylines.therapist_app.ui.materials.models

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun DrawingCanvas(
    strokes: List<DrawingStroke>,
    currentStroke: List<Offset>,
    smoothingEnabled: Boolean,
    straightLineEnabled: Boolean,
    straightLineStart: Offset?
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        strokes.forEach {
            drawPoints(
                points = it.points,
                smoothingEnabled = smoothingEnabled,
                strokeWidth = 10.dp.toPx(),
                singlePointRadius = 4.dp.toPx()
            )
        }

        if (straightLineEnabled && straightLineStart != null) {
            if (currentStroke.isNotEmpty()) {
                drawLine(
                    color = Color.Black,
                    start = straightLineStart,
                    end = currentStroke.last(),
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        } else if (currentStroke.isNotEmpty()) {
            drawPoints(
                points = currentStroke,
                smoothingEnabled = smoothingEnabled,
                strokeWidth = 10.dp.toPx(),
                singlePointRadius = 4.dp.toPx()
            )
        }
    }
}
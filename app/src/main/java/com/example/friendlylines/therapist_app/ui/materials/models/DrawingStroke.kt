package com.example.friendlylines.therapist_app.ui.materials.models

import com.example.friendlylines.therapist_app.ui.materials.geometry.toNormalized
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.shared.data.models.Point

data class DrawingStroke(
    val points: List<Offset>
)

fun DrawingStroke.toNormalizedStroke(
    canvasSize: IntSize
): List<Point> {
    return points.map { point ->
        point.toNormalized(canvasSize)
    }
}
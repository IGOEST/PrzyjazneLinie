package com.example.friendlylines.therapist_app.ui.materials.models

import com.example.shared.data.entities.PatternWithStrokes
import com.example.shared.data.models.Point

data class PatternDrawing(
    val strokes: List<List<Point>>
)

fun PatternWithStrokes.toPatternDrawing(): PatternDrawing {
    return PatternDrawing(
        strokes = strokes
            .sortedBy { it.stroke.order }
            .map { stroke ->
                stroke.points
                    .sortedBy { it.order }
                    .map { point ->
                        Point (
                            x = point.x,
                            y = point.y
                        )
                    }
            }
    )
}
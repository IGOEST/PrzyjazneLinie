package com.example.friendlylines.therapist_app.ui.materials.geometry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.shared.data.models.Point

data class Rectangle(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

data class NormalizedBounds(
    val minX: Float,
    val maxX: Float,
    val minY: Float,
    val maxY: Float
) {
    val width: Float
        get() = maxX - minX

    val height: Float
        get() = maxY - minY
}

fun Offset.isInside(size: IntSize): Boolean {
    return x >= 0f &&
            y >= 0f &&
            x <= size.width &&
            y <= size.height
}

fun IntSize.toRectangle(): Rectangle {
    return Rectangle(
        left = 0f,
        top = 0f,
        right = width.toFloat(),
        bottom = height.toFloat()
    )
}

fun clipSegmentToRectangle(
    start: Offset,
    end: Offset,
    rectangle: Rectangle
): Offset {
    val intersections = mutableListOf<Offset>()
    fun intersectVertical(x: Float) {
        if (end.x == start.x) return
        val t = (x - start.x) / (end.x - start.x)
        if (t in 0f..1f) {
            val y = start.y + t * (end.y - start.y)
            if (y in rectangle.top..rectangle.bottom) {
                intersections.add(
                    Offset(x, y)
                )
            }
        }
    }

    fun intersectHorizontal(y: Float) {
        if (end.y == start.y) return
        val t = (y - start.y) / (end.y - start.y)
        if (t in 0f..1f) {
            val x = start.x + t * (end.x - start.x)
            if (x in rectangle.left..rectangle.right) {
                intersections.add(
                    Offset(x, y)
                )
            }
        }
    }

    intersectVertical(rectangle.left)
    intersectVertical(rectangle.right)

    intersectHorizontal(rectangle.top)
    intersectHorizontal(rectangle.bottom)

    return intersections.minBy {
        it.minus(start).getDistance()
    }
}

fun calculateNormalizedBounds(
    strokes: List<List<Point>>
): NormalizedBounds? {
    val points = strokes.flatten()

    if (points.isEmpty()) {
        return null
    }

    return NormalizedBounds(
        minX = points.minOf { it.x },
        maxX = points.maxOf { it.x },
        minY = points.minOf { it.y },
        maxY = points.maxOf { it.y }
    )
}
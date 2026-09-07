package com.example.friendlylines.therapist_app.ui.materials.geometry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.shared.data.entities.PointEntity
import com.example.shared.data.models.Point

fun Offset.toNormalized(
    canvasSize: IntSize
): Point {
    require(canvasSize.width > 0)
    require(canvasSize.height > 0)

    val referenceSize = minOf(
        canvasSize.width,
        canvasSize.height
    ).toFloat()

    return Point(
        x = x / referenceSize,
        y = y / referenceSize
    )
}

fun Point.toOffset(
    canvasSize: IntSize
): Offset {
    require(canvasSize.width > 0)
    require(canvasSize.height > 0)

    val referenceSize = minOf(
        canvasSize.width,
        canvasSize.height
    ).toFloat()

    return Offset(
        x = x * referenceSize,
        y = y * referenceSize
    )
}

fun PointEntity.toOffset(
    canvasSize: IntSize
): Offset {
    require(canvasSize.width > 0)
    require(canvasSize.height > 0)

    val referenceSize = minOf(
        canvasSize.width,
        canvasSize.height
    ).toFloat()

    return Offset(
        x = x * referenceSize,
        y = y * referenceSize
    )
}
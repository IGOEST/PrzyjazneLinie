package com.example.friendlylines.therapist_app.ui.materials.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

private fun interpolate(
    a: Offset,
    b: Offset,
    factor: Float
): Offset {
    return Offset(
        x = a.x + (b.x - a.x) * factor,
        y = a.y + (b.y - a.y) * factor
    )
}

fun chaikinIteration(
    points: List<Offset>
): List<Offset> {
    if (points.size < 3) {
        return points
    }

    val result = mutableListOf<Offset>()

    result.add(points.first())

    for (i in 0 until points.lastIndex) {
        val current = points[i]
        val next = points[i + 1]

        val q = interpolate(
            a = current,
            b = next,
            factor = 0.25f
        )

        val r = interpolate(
            a = current,
            b = next,
            factor = 0.75f
        )

        result.add(q)
        result.add(r)
    }

    result.add(points.last())

    return result
}

fun chaikinSmooth(
    points: List<Offset>,
    iterations: Int = 2
): List<Offset> {

    var result = points

    repeat(iterations) {
        result = chaikinIteration(result)
    }

    return result
}

fun createChaikinPath(
    points: List<Offset>,
    iterations: Int = 2
): Path {
    val path = Path()

    if (points.isEmpty()) {
        return path
    }

    if (points.size == 1) {
        return path
    }

    val smoothPoints = chaikinSmooth(
        points = points,
        iterations = iterations
    )

    path.moveTo(
        smoothPoints.first().x,
        smoothPoints.first().y
    )

    for (i in 1 until smoothPoints.size) {
        path.lineTo(
            smoothPoints[i].x,
            smoothPoints[i].y
        )
    }

    return path
}

fun DrawScope.drawPoints(
    points: List<Offset>,
    smoothingEnabled: Boolean,
    strokeWidth: Float,
    singlePointRadius: Float,
    strokeColor: Color = Color.Black
) {
    if (points.isEmpty()) {
        return
    }

    if (points.size == 1) {
        drawCircle(
            color = strokeColor,
            radius = singlePointRadius,
            center = points.first()
        )

        return
    }

    if (!smoothingEnabled) {
        for (i in 0 until points.lastIndex) {
            drawLine(
                color = strokeColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        return
    }

    val path = createChaikinPath(
        points = points,
        iterations = 2
    )

    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
package com.example.friendlylines.therapist_app.ui.materials.create_new.componenets

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.friendlylines.theme.FriendlyLinesColors

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

private fun distance(
    a: Offset,
    b: Offset
): Float {
    return (b - a).getDistance()
}

private fun clamp(
    value: Float,
    min: Float = 0f,
    max: Float = 1f
): Float {
    return value.coerceIn(min, max)
}

fun smoothPoints(
    points: List<Offset>,
    iterations: Int = 3,
    strength: Float = 0.35f
): List<Offset> {

    if (points.size < 3) {
        return points
    }

    var result = points.toList()

    repeat(iterations) {

        val smoothed = result.toMutableList()

        for (i in 1 until result.lastIndex) {

            val previous = result[i - 1]
            val current = result[i]
            val next = result[i + 1]

            val midpoint = Offset(
                x = (previous.x + next.x) / 2f,
                y = (previous.y + next.y) / 2f
            )

            val incoming = current - previous
            val outgoing = next - current

            val incomingLength = incoming.getDistance()
            val outgoingLength = outgoing.getDistance()

            if (incomingLength < 0.001f ||
                outgoingLength < 0.001f
            ) {
                continue
            }

            val dot = (
                    incoming.x * outgoing.x +
                            incoming.y * outgoing.y
                    ) / (incomingLength * outgoingLength)

            val curvature = 1f - clamp(dot, -1f, 1f)

            val adaptiveStrength = strength *
                    (1f - curvature * 0.85f)

            smoothed[i] = interpolate(
                current,
                midpoint,
                adaptiveStrength
            )
        }

        result = smoothed
    }

    return result
}

fun createSmoothPath(
    points: List<Offset>,
    iterations: Int = 3,
    strength: Float = 0.35f
): Path {

    val path = Path()

    if (points.isEmpty()) {
        return path
    }

    if (points.size == 1) {
        path.moveTo(points.first().x, points.first().y)
        return path
    }

    val smooth = smoothPoints(
        points = points,
        iterations = iterations,
        strength = strength
    )

    path.moveTo(
        smooth.first().x,
        smooth.first().y
    )

    for (i in 1 until smooth.lastIndex) {

        val current = smooth[i]
        val next = smooth[i + 1]

        val midpoint = interpolate(
            current,
            next,
            0.5f
        )

        path.quadraticTo(
            current.x,
            current.y,
            midpoint.x,
            midpoint.y
        )
    }

    path.lineTo(
        smooth.last().x,
        smooth.last().y
    )

    return path
}

fun DrawScope.drawPoints(
    points: List<Offset>,
    smoothingEnabled: Boolean,
    strokeWidth: Float,
    singlePointRadius: Float,
    strokeColor: Color = FriendlyLinesColors.Shades.Black
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
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
        }

        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        return
    }

    val path = createSmoothPath(
        points = points,
        iterations = 3,
        strength = 0.35f
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
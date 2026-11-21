package com.example.friendlylines.therapist_app.ui.materials.create_new.componenets

import com.example.shared.data.models.Point
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

data class PatternTransform(
    val scale: Float,
    val rotation: Float
)

enum class TriangleType {
    EXAMPLE,
    EQUILATERAL,
    ISOSCELES,
    SCALENE,
    ACUTE,
    RIGHT,
    OBTUSE
}

fun canRotate(patternKey: String?): Boolean {
    return patternKey !in setOf(
        "vertical_line",
        "horizontal_line",
        "square",
        "rectangle",
        "circle"
    )
}

fun getTriangleType(varietyKey: Int): TriangleType {
    val types = TriangleType.entries

    return types[Math.floorMod(varietyKey, types.size)]
}

private data class TriangleSides(
    val a: Double,
    val b: Double,
    val c: Double
)

private data class TriangleVertex(
    val x: Double,
    val y: Double
)

private fun isValidTriangle(sides: TriangleSides, type: TriangleType): Boolean {

    val (a, b, c) = sides

    if (a <= 0.0 || b <= 0.0 || c <= 0.0) {
        return false
    }

    if (a + b <= c || a + c <= b || b + c <= a) {
        return false
    }

    val largestSquared = maxOf(a * a, b * b, c * c)
    val remainingSquared = a * a + b * b + c * c - largestSquared

    val tolerance = maxOf(a, b, c) * 0.000001

    val abEqual = abs(a - b) <= tolerance
    val bcEqual = abs(b - c) <= tolerance
    val acEqual = abs(a - c) <= tolerance

    return when (type) {
        TriangleType.EXAMPLE -> true

        TriangleType.EQUILATERAL ->
            abEqual && bcEqual && acEqual

        TriangleType.ISOSCELES ->
            (abEqual || bcEqual || acEqual) && !(abEqual && bcEqual && acEqual)

        TriangleType.SCALENE ->
            !abEqual && !bcEqual && !acEqual

        TriangleType.ACUTE ->
            largestSquared < remainingSquared

        TriangleType.RIGHT ->
            abs(largestSquared - remainingSquared) < largestSquared * 0.00000001

        TriangleType.OBTUSE ->
            largestSquared > remainingSquared
    }
}

private fun generateTriangleSides(type: TriangleType, random: Random): TriangleSides {

    if (type == TriangleType.EQUILATERAL) {
        val side = random.nextDouble(45.0, 90.0)
        return TriangleSides(side, side, side)
    }

    if (type == TriangleType.ISOSCELES) {
        val equalSide = random.nextDouble(45.0, 85.0)
        val base = equalSide * random.nextDouble(0.65, 1.45)
        return TriangleSides(equalSide, equalSide, base
        )
    }

    if (type == TriangleType.RIGHT) {
        val legA = random.nextDouble(35.0, 70.0)
        val legB = random.nextDouble(35.0, 70.0)
        val hypotenuse = sqrt(legA * legA + legB * legB)
        return TriangleSides(legA, legB, hypotenuse)
    }

    repeat(100) {
        val values = listOf(
            random.nextDouble(35.0, 90.0),
            random.nextDouble(35.0, 90.0),
            random.nextDouble(35.0, 90.0)
        ).sorted()

        val sides = TriangleSides(values[0], values[1], values[2])

        if (!isValidTriangle(sides, type)) {
            return@repeat
        }

        val a = sides.a
        val b = sides.b
        val c = sides.c

        val areaSquared = ((a + b + c) * (-a + b + c) * (a - b + c) * (a + b - c)) / 16.0
        val area = sqrt(areaSquared)
        val minimumHeight = 2.0 * area / c

        if (minimumHeight < 15.0) {
            return@repeat
        }

        val largestSquared = c * c
        val otherSquared = a * a + b * b

        if (type == TriangleType.ACUTE && largestSquared >= otherSquared * 0.92
        ) {
            return@repeat
        }

        if (type == TriangleType.OBTUSE && largestSquared <= otherSquared * 1.08
        ) {
            return@repeat
        }

        return sides
    }

    return when (type) {

        TriangleType.EXAMPLE -> TriangleSides(50.0, 65.0, 80.0)

        TriangleType.SCALENE -> TriangleSides(45.0, 60.0, 80.0)

        TriangleType.ACUTE -> TriangleSides(60.0, 70.0, 80.0)

        TriangleType.OBTUSE -> TriangleSides(40.0, 50.0, 75.0)

        else -> TriangleSides(50.0, 60.0, 70.0)
    }
}

private fun createVertices(sides: TriangleSides): List<TriangleVertex> {
    val (a, b, c) = sides

    val x = (b * b + c * c - a * a) / (2.0 * c)

    val y = sqrt(maxOf(0.0, b * b - x * x))

    return listOf(
        TriangleVertex(0.0, 0.0),
        TriangleVertex(c, 0.0),
        TriangleVertex(x, y)
    )
}

private fun normalizeVertices(vertices: List<TriangleVertex>, random: Random): List<Point> {
    val angle = Math.toRadians(
        random.nextDouble(0.0, 360.0)
    )

    val cos = cos(angle)
    val sin = sin(angle)

    val mirror = if (random.nextBoolean()) -1.0 else 1.0

    return vertices.map { point ->
        Point(
            x = ((point.x * cos - point.y * sin) * mirror).toFloat(),
            y = (point.x * sin + point.y * cos).toFloat()
        )
    }
}

fun createTriangle(type: TriangleType, varietyKey: Int = Random.nextInt()): List<List<Point>> {
    val random = Random(varietyKey)

    val sides = generateTriangleSides(type = type, random = random)

    check(isValidTriangle(sides, type))

    val vertices = createVertices(sides)

    val points = normalizeVertices(
        vertices = vertices,
        random = random
    )

    return listOf(points + points.first())
}



package com.example.shared.data.examples

import com.example.shared.data.models.Point

data class ExamplePattern(
    val key: String,
    val name: String,
    val isComplex: Boolean,
    val smoothingEnabled: Boolean,
    val strokes: List<List<Point>>
)

object ExamplePatterns {
    val all: List<ExamplePattern> = listOf(
        ExamplePattern(
            key = "vertical_line",
            name = "Vertical line",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleVerticalLine()
        ),
        ExamplePattern(
            key = "horizontal_line",
            name = "Horizontal line",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleHorizontalLine()
        ),
        ExamplePattern(
            key = "diagonal_line_down",
            name = "Diagonal line down",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleDiagonalLineDown()
        ),
        ExamplePattern(
            key = "diagonal_line_up",
            name = "Diagonal line up",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleDiagonalLineUp()
        ),
        ExamplePattern(
            key = "curve_line",
            name = "Curve line",
            isComplex = false,
            smoothingEnabled = true,
            strokes = exampleCurveLine()
        ),
        ExamplePattern(
            key = "square",
            name = "Square",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleSquare()
        ),
        ExamplePattern(
            key = "rectangle",
            name = "Rectangle",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleRectangle()
        ),
        ExamplePattern(
            key = "triangle",
            name = "Triangle",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleTriangle()
        ),
        ExamplePattern(
            key = "circle",
            name = "Circle",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleCircle()
        ),
        ExamplePattern(
            key = "ellipse",
            name = "Ellipse",
            isComplex = false,
            smoothingEnabled = false,
            strokes = exampleEllipse()
        )
    )

    private fun exampleVerticalLine(): List<List<Point>> {
        return listOf(
            listOf(
                Point(50f, 0f),
                Point(50f, 100f)
            )
        )
    }

    private fun exampleHorizontalLine(): List<List<Point>> {
        return listOf(
            listOf(
                Point(0f, 50f),
                Point(100f, 50f)
            )
        )
    }

    private fun exampleDiagonalLineDown(): List<List<Point>> {
        return listOf(
            listOf(
                Point(0f, 0f),
                Point(100f, 100f)
            )
        )
    }

    private fun exampleDiagonalLineUp(): List<List<Point>> {
        return listOf(
            listOf(
                Point(0f, 100f),
                Point(100f, 0f)
            )
        )
    }

    private fun exampleCurveLine(): List<List<Point>> {
        val points = mutableListOf<Point>()

        val centerX = 50f
        val topY = 15f
        val bottomY = 85f
        val radiusX = 35f
        val radiusY = 35f
        val numberOfPoints = 48

        points.add(Point(centerX - radiusX, topY))

        for (i in 0..numberOfPoints) {
            val angle = 2f * Math.PI.toFloat() * i / numberOfPoints

            points.add(
                Point(
                    x = centerX + radiusX * kotlin.math.cos(angle),
                    y = topY + radiusY * kotlin.math.sin(angle)
                )
            )
        }

        points.add(Point(centerX + radiusX, topY))
        return listOf(points)
    }

    private fun exampleSquare(): List<List<Point>> {
        return listOf(
            listOf(
                Point(0f, 0f),
                Point(100f, 0f),
                Point(100f, 100f),
                Point(0f, 100f),
                Point(0f, 0f)
            )
        )
    }

    private fun exampleRectangle(): List<List<Point>> {
        return listOf(
            listOf(
                Point(20f, 10f),
                Point(80f, 10f),
                Point(80f, 90f),
                Point(20f, 90f),
                Point(20f, 10f)
            )
        )
    }

    private fun exampleTriangle(): List<List<Point>> {
        return listOf(
            listOf(
                Point(50f, 0f),
                Point(100f, 100f),
                Point(0f, 100f),
                Point(50f, 0f)
            )
        )
    }

    private fun exampleCircle(): List<List<Point>> {
        val points = mutableListOf<Point>()

        val centerX = 50f
        val centerY = 50f
        val radius = 50f

        val numberOfPoints = 64

        for (i in 0..numberOfPoints) {
            val angle = 2f * Math.PI.toFloat() * i / numberOfPoints

            points.add(
                Point(
                    x = centerX + radius * kotlin.math.cos(angle),
                    y = centerY + radius * kotlin.math.sin(angle)
                )
            )
        }

        return listOf(points)
    }

    private fun exampleEllipse(): List<List<Point>> {
        val points = mutableListOf<Point>()

        val centerX = 50f
        val centerY = 50f
        val radiusX = 40f
        val radiusY = 25f

        val numberOfPoints = 64

        for (i in 0..numberOfPoints) {
            val angle = 2f * Math.PI.toFloat() * i / numberOfPoints

            points.add(
                Point(
                    x = centerX + radiusX * kotlin.math.cos(angle),
                    y = centerY + radiusY * kotlin.math.sin(angle)
                )
            )
        }

        return listOf(points)
    }
}
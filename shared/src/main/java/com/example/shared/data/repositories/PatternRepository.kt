package com.example.shared.data.repositories

import com.example.shared.data.daos.PatternDao
import com.example.shared.data.entities.PatternEntity
import com.example.shared.data.entities.PatternWithStrokes
import com.example.shared.data.entities.PointEntity
import com.example.shared.data.entities.StrokeEntity
import com.example.shared.data.models.Point
import javax.inject.Inject

private const val EXAMPLE_CANVAS_SIZE = 1000f

private data class ExamplePattern(
    val key: String,
    val name: String,
    val isComplex: Boolean,
    val smoothingEnabled: Boolean,
    val strokes: List<List<Point>>
)

class PatternRepository @Inject constructor(
    private val patternDao: PatternDao
) {
    suspend fun savePattern(
        name: String,
        isExample: Boolean,
        isComplex: Boolean,
        createdAt: Long,
        strokes: List<List<Point>>,
        smoothingEnabled: Boolean,
        exampleKey: String? = null
    ): Long {
        val pattern = PatternEntity(
            name = name,
            isExample = isExample,
            isComplex = isComplex,
            createdAt = createdAt,
            smoothingEnabled = smoothingEnabled,
            exampleKey = exampleKey
        )

        val strokeEntities = strokes.mapIndexed { index, _ ->
            StrokeEntity(
                patternId = 0,
                order = index
            )
        }

        val pointsByStroke = strokes.map { stroke ->
            stroke.mapIndexed { index, point ->
                PointEntity(
                    strokeId = 0,
                    order = index,
                    x = point.x,
                    y = point.y
                )
            }
        }

        return patternDao.insertPatternWithStrokes(
            pattern = pattern,
            strokes = strokeEntities,
            pointsByStroke = pointsByStroke
        )
    }

    suspend fun getAllPatterns(): List<PatternEntity> {
        return patternDao.getAllPatterns()
    }

    suspend fun getPattern(patternId: Long): PatternWithStrokes? {
        return patternDao.getPatternWithStrokes(patternId)
    }

    suspend fun deletePattern(patternId: Long) {
        patternDao.deletePatternById(patternId)
    }

    suspend fun patternExistsByName(name: String): Boolean {
        return patternDao.patternExistsByName(name)
    }

    suspend fun insertExamplePatterns() {
        val examplePatterns = examplePatterns()
        examplePatterns.forEachIndexed { index, example ->
            if (!patternDao.examplePatternExists(example.key)) {
                val normalizedStrokes = example.strokes.map { stroke ->
                    stroke
                        .scaleToReferenceSize(EXAMPLE_CANVAS_SIZE)
                        .map { point ->
                        point.normalize(EXAMPLE_CANVAS_SIZE)
                    }
                }
                savePattern(
                    name = example.name,
                    isExample = true,
                    isComplex = example.isComplex,
                    createdAt = index.toLong(),
                    strokes = normalizedStrokes,
                    smoothingEnabled = example.smoothingEnabled,
                    exampleKey = example.key
                )
            }
        }
    }

    private fun examplePatterns(): List<ExamplePattern> {
        return listOf(
            ExamplePattern(
                key = "square",
                name = "Square",
                isComplex = false,
                smoothingEnabled = false,
                strokes = exampleSquare()
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
            )
        )
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

    fun Point.normalize(
        referenceSize: Float
    ): Point {
        require(referenceSize > 0f)

        return Point(
            x = x / referenceSize,
            y = y / referenceSize
        )
    }

    fun List<Point>.scaleToReferenceSize(
        referenceSize: Float,
        margin: Float = 0.1f
    ): List<Point> {
        if (isEmpty()) return this

        val minX = minOf { it.x }
        val maxX = maxOf { it.x }
        val minY = minOf { it.y }
        val maxY = maxOf { it.y }

        val width = maxX - minX
        val height = maxY - minY

        if (width <= 0f && height <= 0f) {
            return this
        }

        val targetSize = referenceSize * (1f - 2f * margin)

        val scale = targetSize / maxOf(width, height)

        val scaledWidth = width * scale
        val scaledHeight = height * scale

        val offsetX = (referenceSize - scaledWidth) / 2f
        val offsetY = (referenceSize - scaledHeight) / 2f

        return map { point ->
            Point(
                x = (point.x - minX) * scale + offsetX,
                y = (point.y - minY) * scale + offsetY
            )
        }
    }
}
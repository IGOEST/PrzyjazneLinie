package com.example.shared.data.repositories

import com.example.shared.data.daos.PatternDao
import com.example.shared.data.entities.PatternEntity
import com.example.shared.data.entities.PatternWithStrokes
import com.example.shared.data.entities.PointEntity
import com.example.shared.data.entities.StrokeEntity
import com.example.shared.data.models.Point
import com.example.shared.data.examples.ExamplePatterns
import javax.inject.Inject

private const val EXAMPLE_CANVAS_SIZE = 1000f

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

    suspend fun getExamplePatternByKey(key: String): PatternEntity? {
        return patternDao.getExamplePatternByKey(key)
    }

    suspend fun deletePattern(patternId: Long) {
        patternDao.deletePatternById(patternId)
    }

    suspend fun patternExistsByName(name: String): Boolean {
        return patternDao.patternExistsByName(name)
    }

    suspend fun insertExamplePatterns() {
        ExamplePatterns.all.forEachIndexed { index, example ->
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
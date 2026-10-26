package com.example.friendlylines.child_app.assessment

import androidx.compose.ui.unit.IntSize
import com.example.shared.data.models.Point
import kotlin.math.sqrt

data class ShapeAssessmentResult(
    val score: Float,   // 0.0 = very different, 1.0 = identical
    val distance: Float,    // lower = better match
    val isCorrect: Boolean,
    val isValid: Boolean
)

/**
 * Using Procrustes analysis for shape comparison
 * - dividing every stroke to equal number of points
 * - centering and scaling whole drawing
 * - comparing user strokes and reference strokes
 * - calculating remaining error
 * (stroke order is ignored)
 */
class ShapeCorrectness (
    private val sampleCount: Int = 32,
    private val correctThreshold: Float = 0.80f,
    private val maxStrokesToMatch: Int = 8
) {
    init {
        require(sampleCount >= 2)
        require(correctThreshold in 0f..1f)
        require(maxStrokesToMatch >= 1)
    }

    fun evaluate(
        userStrokes: List<List<Point>>,
        referenceStrokes: List<List<Point>>,
        canvasSize: IntSize
    ): ShapeAssessmentResult {

        if (userStrokes.isEmpty() || referenceStrokes.isEmpty()) {
            return invalidResult()
        }

        if (userStrokes.size != referenceStrokes.size) {
            return invalidResult()
        }

        if (userStrokes.size > maxStrokesToMatch) {
            return invalidResult()
        }

        val userPoints = userStrokes.map { stroke ->
            resample(stroke) ?: return invalidResult()
        }

        val referencePoints = referenceStrokes.map { stroke ->
            resample(stroke) ?: return invalidResult()
        }

        // normalize the whole drawing, preserving position of strokes relative to each other
        val normalizedUser = normalize(userPoints)
            ?: return invalidResult()

        val normalizedReference = normalize(referencePoints)
            ?: return invalidResult()

        android.util.Log.d(
            "ShapeCorrectness",
            "user first point=${normalizedUser.first().first()}, " +
                    "reference first point=${normalizedReference.first().first()}"
        )

        // find the best pairing of user strokes to reference strokes
        val bestDistance = findBestAssignment(
            user = normalizedUser,
            reference = normalizedReference
        )

        if (!bestDistance.isFinite()) return invalidResult()

        val referenceScale = calculateScale(normalizedReference)

        android.util.Log.d(
            "ShapeCorrectness",
            "bestDistance=$bestDistance, referenceScale=$referenceScale"
        )

        val score = (1.0 - bestDistance / referenceScale)
            .coerceIn(0.0, 1.0)
            .toFloat()

        android.util.Log.d("ShapeCorrectness", "score=$score")

        return ShapeAssessmentResult(
            score = score,
            distance = bestDistance.toFloat(),
            isCorrect = score >= correctThreshold,
            isValid = true
        )
    }

    private fun calculateScale(strokes: List<List<Point>>): Double {
    val points = strokes.flatten()

    val minX = points.minOf { it.x.toDouble() }
    val maxX = points.maxOf { it.x.toDouble() }
    val minY = points.minOf { it.y.toDouble() }
    val maxY = points.maxOf { it.y.toDouble() }

    android.util.Log.d(
        "ShapeCorrectness",
        "minX=$minX maxX=$maxX minY=$minY maxY=$maxY"
    )

    return sqrt(
        (maxX - minX) * (maxX - minX) +
        (maxY - minY) * (maxY - minY)
    ).coerceAtLeast(1e-6)
}

    // resampling one stroke, so that they consist of the same number of points within same distance
    private fun resample(
        points: List<Point>
    ): List<Point>? {
        if (points.size < 2) return null

        val cumulative = DoubleArray(points.size)   // stores the path length from the first point up to ith point
        var totalLength = 0.0

        for (i in 1 until points.size) {
            val dx = (points[i].x - points[i - 1].x).toDouble()
            val dy = (points[i].y - points[i - 1].y).toDouble()

            totalLength += sqrt(dx * dx + dy * dy)
            cumulative[i] = totalLength
        }

        if(totalLength < 1e-6) return null  // rejecting stroke where every point is in the same place

        val result = ArrayList<Point>(sampleCount)
        var segment = 1

        for (i in 0 until sampleCount) {
            // first sample at the start, last at the end
            val target = totalLength*i.toDouble()/(sampleCount - 1)

            // find segment containing this distance
            while (
                segment < points.lastIndex &&
                cumulative[segment] < target
            ) {
                segment ++
            }
            val segmentStart = cumulative[segment - 1]
            val segmentLength = cumulative[segment] - segmentStart

            // t=0 -> start of the segment, t=1 -> end
            val t = if (segmentLength > 1e-9) {
                ((target - segmentStart)/segmentLength).coerceIn(0.0, 1.0)
            } else {
                0.0
            }

            val a = points[segment - 1]
            val b = points[segment]

            result.add(
                Point(
                    x = (a.x + (b.x - a.x) * t).toFloat(),
                    y = (a.y + (b.y - a.y) * t).toFloat()
                )
            )
        }

        return result
    }

    // centering and all strokes together (= drawing in different part of the screen is allowed, difference in size is not allowed)
    private fun normalize(
        strokes: List<List<Point>>
    ): List<List<Point>>? {
        val allPoints = strokes.flatten()
        if (allPoints.isEmpty()) return null

        val centerX = allPoints.map { it.x.toDouble() }.average()
        val centerY = allPoints.map { it.y.toDouble() }.average()

        val centeredStrokes = strokes.map { stroke ->
            stroke.map { point ->
                Point(
                    x = (point.x - centerX).toFloat(),
                    y = (point.y - centerY).toFloat()
                )
            }
        }

        return centeredStrokes
    }

    // trying every possible assignment of strokes
    private fun findBestAssignment(
        user: List<List<Point>>,
        reference: List<List<Point>>
    ): Double {

        val usedReference = BooleanArray(reference.size)
        var bestDistance = Double.POSITIVE_INFINITY

        // at the beginning mapping user stroke to a reference by index
        val assignment = IntArray(user.size)

        fun search(userIndex: Int) {
            // if every user stroke has been assigned -> evaluating pairing
            if (userIndex == user.size) {
                val orderedReference = assignment.map { reference[it] }

                val distance = compareDrawings(
                    user = user,
                    reference = orderedReference
                )

                if (distance < bestDistance) {
                    bestDistance = distance
                }
                return
            }

            // trying not used reference strokes
            for (referenceIndex in reference.indices) {
                if (usedReference[referenceIndex]) continue

                usedReference[referenceIndex] = true
                assignment[userIndex] = referenceIndex

                search(userIndex + 1)

                usedReference[referenceIndex] = false
            }
        }

        search(0)
        return bestDistance
    }

    private fun calculateStrokeError(
        userStroke: List<Point>,
        referenceStroke: List<Point>
    ): Double {
        val n = userStroke.size

        if (n != referenceStroke.size || n == 0) {
            return Double.POSITIVE_INFINITY
        }

        fun isClosed(stroke: List<Point>): Boolean {
            val first = stroke.first()
            val last = stroke.last()

            val minX = stroke.minOf { it.x }
            val maxX = stroke.maxOf { it.x }
            val minY = stroke.minOf { it.y }
            val maxY = stroke.maxOf { it.y }

            val dx = (maxX - minX).toDouble()
            val dy = (maxY - minY).toDouble()
            val diagonal = sqrt(dx * dx + dy * dy)

            val endDx = (first.x - last.x).toDouble()
            val endDy = (first.y - last.y).toDouble()
            val endDistance = sqrt(endDx * endDx + endDy * endDy)

            return endDistance <= (diagonal * 0.1).coerceAtLeast(1e-4)
        }

        fun errorForShift(shift: Int, reversed: Boolean): Double {
            var error = 0.0

            for (i in 0 until n) {
                val referenceIndex = if (reversed) {
                    (shift - i + n) % n
                } else {
                    (i + shift) % n
                }

                val user = userStroke[i]
                val reference = referenceStroke[referenceIndex]

                val dx = user.x.toDouble() - reference.x
                val dy = user.y.toDouble() - reference.y

                error += dx * dx + dy * dy
            }

            return error
        }

        // for "open" patterns we assume starting points on the edge of patterns
        if (!isClosed(userStroke) || !isClosed(referenceStroke)) {
            return minOf(
                errorForShift(0, reversed = false),
                errorForShift(n - 1, reversed = true)
            )
        }

        // for close patterns we have to test all possible starting points
        var bestError = Double.POSITIVE_INFINITY

        for (shift in 0 until n) {
            bestError = minOf(
                bestError,
                errorForShift(shift, reversed = false),
                errorForShift(shift, reversed = true)
            )
        }

        return bestError
    }

    // comparing 2 drawings whose strokes are already paired
    private fun compareDrawings(
        user: List<List<Point>>,
        reference: List<List<Point>>
    ): Double {

        var totalSquaredError = 0.0
        var totalPoints = 0

        for (strokeIndex in user.indices) {
            val userStroke = user[strokeIndex]
            val referenceStroke= reference[strokeIndex]

            if (userStroke.size != referenceStroke.size) {
                return Double.POSITIVE_INFINITY
            }

            var strokeError = calculateStrokeError(
                userStroke = userStroke,
                referenceStroke = referenceStroke
            )

            if (!strokeError.isFinite()) {
                return Double.POSITIVE_INFINITY
            }

            totalSquaredError += strokeError
            totalPoints += userStroke.size
        }

        if (totalPoints == 0) return Double.POSITIVE_INFINITY
        return sqrt(totalSquaredError / totalPoints)
    }

    // invalid = not enough usable input to compare
    private fun invalidResult() = ShapeAssessmentResult(
        score = 0f,
        distance = Float.POSITIVE_INFINITY,
        isCorrect = false,
        isValid = false
    )

}
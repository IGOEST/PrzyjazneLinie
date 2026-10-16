package com.example.friendlylines.child_app.assessment

import com.example.shared.data.models.Point
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
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
        allowRotation: Boolean = false
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

        // find the best pairing of user strokes to reference strokes
        val bestDistance = findBestAssignment(
            user = normalizedUser,
            reference = normalizedReference,
            allowRotation = allowRotation
        )

        val score = (1.0 - bestDistance / 2.0)
            .coerceIn(0.0, 1.0)
            .toFloat()

        return ShapeAssessmentResult(
            score = score,
            distance = bestDistance.toFloat(),
            isCorrect = score >= correctThreshold,
            isValid = true
        )
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

    // centering and scaling all strokes together (= drawing in different part of the screen is allowed, difference in size is allowed)
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

        // total size of entire drawing after centering
        val sizeSquared = centeredStrokes.flatten().sumOf { point ->
            point.x.toDouble() * point.x +
                point.y.toDouble() * point.y
        }

        val size = sqrt(sizeSquared)
        if (size < 1e-9) return null

        return centeredStrokes.map { stroke ->
            stroke.map { point ->
                Point(
                    x = (point.x / size).toFloat(),
                    y = (point.y / size).toFloat()
                )
            }
        }
    }

    // trying every possible assignment of strokes
    private fun findBestAssignment(
        user: List<List<Point>>,
        reference: List<List<Point>>,
        allowRotation: Boolean
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
                    reference = orderedReference,
                    allowRotation = allowRotation
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

    // comparing 2 drawings whose strokes are already paired
    private fun compareDrawings(
        user: List<List<Point>>,
        reference: List<List<Point>>,
        allowRotation: Boolean
    ): Double {

        val flatUser = user.flatten()
        val flatReference = reference.flatten()

        val angle = if (allowRotation) {
            bestRotationAngle(flatUser, flatReference)
        } else {
            0.0
        }

        val c = cos(angle)
        val s = sin(angle)
        var squaredError = 0.0

        for (i in flatUser.indices) {
            val point = flatUser[i]
            val target = flatReference[i]

            // applying the same rotation to every point in the user drawing
            val rotatedX = point.x * c - point.y * s
            val rotatedY = point.x * s + point.y * c

            val dx = rotatedX - target.x
            val dy = rotatedY - target.y

            squaredError += dx * dx + dy * dy
        }

        // root mean square error
        return sqrt(squaredError / flatUser.size)
    }

    // calculating angle that minimizes squared errors between points
    private fun bestRotationAngle(
        user: List<Point>,
        reference: List<Point>
    ): Double {

        var dot = 0.0
        var cross = 0.0

        for (i in user.indices) {
            val ux = user[i].x.toDouble()
            val uy = user[i].y.toDouble()
            val rx = reference[i].x.toDouble()
            val ry = reference[i].y.toDouble()

            // determining the rotation that best aligns the points
            dot += ux * rx + uy * ry
            cross += ux * ry - uy * rx
        }

        return atan2(cross, dot)
    }

    // invalid = not enough usable input to compare
    private fun invalidResult() = ShapeAssessmentResult(
        score = 0f,
        distance = Float.POSITIVE_INFINITY,
        isCorrect = false,
        isValid = false
    )

}
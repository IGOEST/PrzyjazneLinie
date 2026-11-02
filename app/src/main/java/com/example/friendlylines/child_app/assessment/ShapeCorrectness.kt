package com.example.friendlylines.child_app.assessment

import com.example.shared.data.models.Point
import kotlin.math.acos
import kotlin.math.sqrt

data class ShapeAssessmentResult(
    val score: Float,   // 0.0 = very different, 1.0 = identical
    val distance: Float,    // lower = better match
    val isValid: Boolean
)

/**
 * Using Procrustes algorithm inspired analysis for shape comparison
 * - dividing every stroke to equal number of points
 * - centering and scaling whole drawing
 * - comparing user strokes and reference strokes, finding best match
 * - calculating remaining error (positional and directional differences)
 * (stroke order is ignored)
 */
class ShapeCorrectness (
    private val minSampleCount: Int = 32,
    private val maxSampleCount: Int = 1024,
    private val sampleCountMultiplier: Double = 100.0,
    private val maxStrokesToMatch: Int = 8,
    private val turnImportance: Double = 0.5
) {
    init {
        require(maxStrokesToMatch >= 1)
    }
    /**
     * MAIN EVALUATION
     */
    fun evaluateShape(
        userStrokes: List<List<Point>>,
        referenceStrokes: List<List<Point>>
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

        val userPoints = mutableListOf<List<Point>>()
        val referencePoints = mutableListOf<List<Point>>()

        for (i in userStrokes.indices) {
            val userStroke = userStrokes[i]
            val referenceStroke = referenceStrokes[i]

            val sampleCount = ((userStrokes + referenceStrokes).maxOf { calculatePointCount(it) }).coerceIn(minSampleCount, maxSampleCount)

            val sampledUser = resample(
                points = userStroke,
                sampleCount = sampleCount
            ) ?: return invalidResult()

            val sampledReference = resample(
                points = referenceStroke,
                sampleCount = sampleCount
            ) ?: return invalidResult()

            userPoints.add(sampledUser)
            referencePoints.add(sampledReference)
        }

        // normalize the whole drawing, preserving position of strokes relative to each other
        val normalizedUser = normalize(userPoints)
            ?: return invalidResult()

        val normalizedReference = normalize(referencePoints)
            ?: return invalidResult()

        // find the best pairing of user strokes to reference strokes
        val bestDistance = findBestAssignment(
            user = normalizedUser,
            reference = normalizedReference
        )

        if (!bestDistance.isFinite()) return invalidResult()

        val referenceScale = calculateScale(normalizedReference)

        val score = (1.0 - bestDistance / referenceScale)
            .coerceIn(0.0, 1.0)
            .toFloat()

        return ShapeAssessmentResult(
            score = score,
            distance = bestDistance.toFloat(),
            isValid = true
        )
    }

    /**
     * PREPROCESSING
     */
    private fun calculatePointCount(stroke: List<Point>) : Int {
        val strokeLength = calculateStrokeLength(stroke)
        val complexity = calculateStrokeComplexity(stroke)

        val rawSampleCount = strokeLength * sampleCountMultiplier * complexity

        val sampleCount = rawSampleCount
            .toInt()
            .coerceIn(minSampleCount, maxSampleCount)

        return sampleCount
    }

    private fun calculateStrokeComplexity(stroke: List<Point>): Double {
        if (stroke.size < 3) return 1.0

        var totalDirectionChanges = 0.0
        val noise = Math.toRadians(10.0)

        for (i in 1 until stroke.lastIndex) {
            val turn = calculateTurn(
                stroke = stroke,
                index = i,
                closed = false
            )

            if (turn > noise) {
                totalDirectionChanges += turn
            }
        }

        val complexity = 1.0 + totalDirectionChanges/ Math.PI
        return complexity
    }

    private fun calculateStrokeLength(stroke: List<Point>): Double {
        if (stroke.size <2) return 0.0
        var length = 0.0

        for (i in 1 until stroke.size) {
            val dx = (stroke[i].x - stroke[i - 1].x).toDouble()
            val dy = (stroke[i].y - stroke[i - 1].y).toDouble()
            length += sqrt(dx*dx + dy*dy)
        }

        return length
    }
    private fun calculateScale(strokes: List<List<Point>>): Double {
    val points = strokes.flatten()

    val minX = points.minOf { it.x.toDouble() }
    val maxX = points.maxOf { it.x.toDouble() }
    val minY = points.minOf { it.y.toDouble() }
    val maxY = points.maxOf { it.y.toDouble() }

    return sqrt(
        (maxX - minX) * (maxX - minX) +
        (maxY - minY) * (maxY - minY)
    ).coerceAtLeast(1e-6)
}

    // resampling one stroke, so that they consist of the same number of points within same distance
    private fun resample(
        points: List<Point>,
        sampleCount: Int
    ): List<Point>? {
        if (points.size < 2) return null

        val cumulative = DoubleArray(points.size)   // stores the path length from the first point up to ith point

        var totalLength = 0.0
        for (i in 1 until points.size) {
            val dx = (points[i].x - points[i - 1].x).toDouble()
            val dy = (points[i].y - points[i - 1].y).toDouble()
            totalLength += sqrt(dx*dx + dy*dy)
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

    /**
     * STROKE MATCHING
     */

    // trying every possible assignment of strokes
    private fun findBestAssignment(
        user: List<List<Point>>,
        reference: List<List<Point>>
    ): Double {
        if (user.size != reference.size || user.isEmpty()) return Double.POSITIVE_INFINITY

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

            val strokeError = compareStrokes(
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

    /**
     * SHAPE COMPARISON
     */
    private fun calculateTurnError(
        userStroke: List<Point>,
        referenceStroke: List<Point>,
        userIndex: Int,
        referenceIndex: Int,
        closed: Boolean,
        turnScale: Double
    ): Double {
        val userTurn = calculateTurn(
            stroke = userStroke,
            index = userIndex,
            closed = closed
        )

        val referenceTurn = calculateTurn(
            stroke = referenceStroke,
            index = referenceIndex,
            closed = closed
        )

        // normalizing to range 0-1
        val normalizedTurnError = (userTurn - referenceTurn)/ Math.PI

        // recalculating with scale
        val turnError = turnScale*normalizedTurnError

        return turnError*turnError
    }

    private fun calculatePositionError(
        user: Point,
        reference: Point
    ): Double {
        val dx = user.x.toDouble() - reference.x
        val dy = user.y.toDouble() - reference.y

        return dx * dx + dy * dy
    }

    private fun combineErrors(
        shift: Int,
        reversed: Boolean,
        closed: Boolean,
        userStroke: List<Point>,
        referenceStroke: List<Point>
    ): Double {
        var totalError = 0.0
        val n = userStroke.size

        for (i in 0 until n) {
            val referenceIndex = if (reversed) {
                (shift - i + n) % n
            } else {
                (i + shift) % n
            }

            val positionError = calculatePositionError(
                user = userStroke[i],
                reference = referenceStroke[referenceIndex]
            )

            val turnError = calculateTurnError(
                userStroke = userStroke,
                referenceStroke = referenceStroke,
                userIndex = i,
                referenceIndex = referenceIndex,
                closed = closed,
                turnScale = turnImportance
            )

            totalError += positionError + turnError
        }

        return totalError
    }

    private fun compareStrokes(
        userStroke: List<Point>,
        referenceStroke: List<Point>
    ): Double {
        val n = userStroke.size

        if (n != referenceStroke.size || n == 0) {
            return Double.POSITIVE_INFINITY
        }

        val userClosed = isClosed(userStroke)
        val referenceClosed = isClosed(referenceStroke)

        if (userClosed != referenceClosed) return Double.POSITIVE_INFINITY

        var bestError = Double.POSITIVE_INFINITY

        if (!userClosed) {
            bestError = minOf(
                combineErrors(shift = 0, reversed = false, closed = false, userStroke, referenceStroke),
                combineErrors(shift = n - 1, reversed = true, closed = false, userStroke, referenceStroke)
            )
        }

        // for close patterns we have to test all possible starting points
        for (shift in 0 until n) {
            bestError = minOf(
                bestError,
                combineErrors(shift, reversed = false, closed = true, userStroke, referenceStroke),
                combineErrors(shift, reversed = true, closed = true, userStroke, referenceStroke)
            )
        }

        return bestError
    }

    /**
     * GEOMETRY HELPERS
     */
    private fun calculateTurn(
        stroke: List<Point>,
        index: Int,
        closed: Boolean
    ): Double {
        if (stroke.size < 3) return 0.0

        if(!closed && (index == 0 || index == stroke.lastIndex)) return 0.0

        val previousIndex = if (index == 0) stroke.lastIndex
        else index - 1

        val nextIndex = if (index == stroke.lastIndex) 0
        else index + 1

        val previous = stroke[previousIndex]
        val current = stroke[index]
        val next = stroke[nextIndex]

        // calculating vectors
        // 1 - from previous point to current
        val x1 = (current.x - previous.x).toDouble()
        val y1 = (current.y - previous.y).toDouble()

        // 2 - from current point to next one
        val x2 = (next.x - current.x).toDouble()
        val y2 = (next.y - current.y).toDouble()

        val length1 = sqrt(x1*x1 + y1*y1)
        val length2 = sqrt(x2*x2 + y2*y2)

        if (length1 < 1e-6 || length2 < 1e-6) return 0.0

        // calculating dot product, dividing by lengths so lengths don't matter
        val scalar = (x1*x2 + y1*y2)/(length1*length2)

        // using acos to return angle
        return acos(scalar.coerceIn(-1.0, 1.0))
    }

    // checks if start and end of line are close to each other
    private fun isClosed(stroke: List<Point>): Boolean {
        if (stroke.size < 3) return false

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

    /**
     * HANDLING INVALID RESULTS
     */
    // invalid = not enough usable input to compare
    private fun invalidResult(): ShapeAssessmentResult {
        return ShapeAssessmentResult(
            score = 0f,
            distance = Float.POSITIVE_INFINITY,
            isValid = false
        )
    }

}
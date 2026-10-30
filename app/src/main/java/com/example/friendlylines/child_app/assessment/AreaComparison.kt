package com.example.friendlylines.child_app.assessment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.friendlylines.therapist_app.ui.materials.geometry.calculateNormalizedBounds
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.chaikinSmooth
import com.example.shared.data.models.PatternDrawing
import java.util.Arrays
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Result of drawing and pattern area comparison.
 * - patternCoveragePercent: % of the target pattern area covered by the user's drawing.
 * - drawingOutsidePercent: % of the user's drawing that is outside of the pattern.
 * - drawingPrecisionPercent: % of the user's drawing that is inside of the pattern.
 */
data class AreaComparisonResult(
    val patternCoveragePercent: Float,
    val drawingOutsidePercent: Float,
    val drawingPrecisionPercent: Float,
    val totalPatternAreaPx: Int,
    val totalUserDrawnAreaPx: Int,
    val coveredPatternAreaPx: Int,
    val outsideDrawnAreaPx: Int
)

object AreaComparison {
    enum class AlignmentMode { NONE, TRANSLATE, TRANSLATE_AND_SCALE }

    private const val ALPHA_THRESHOLD = 10

    fun evaluatePatternDrawing(
        userStrokes: List<List<Offset>>,
        patternDrawing: PatternDrawing,
        canvasSize: IntSize,
        patternThicknessPx: Float = 20f,
        userThicknessPx: Float = 20f,
        toleranceMarginPx: Int = 10,
        density: Float = 1f,
        scaleMultiplier: Float = 1f,
        rotationDegrees: Float = 0f,
        smoothing: Boolean = false,
        alignmentMode: AlignmentMode = AlignmentMode.NONE
    ): AreaComparisonResult {
        val patternStrokes = patternDrawing.toCanvasPixelStrokes(
            canvasSize = canvasSize,
            density = density,
            scaleMultiplier = scaleMultiplier,
            rotationDegrees = rotationDegrees,
            smoothing = smoothing
        )
        return evaluateStrokes(
            userStrokes = userStrokes,
            patternStrokes = patternStrokes,
            canvasSize = canvasSize,
            patternThicknessPx = patternThicknessPx,
            userThicknessPx = userThicknessPx,
            toleranceMarginPx = toleranceMarginPx,
            alignmentMode = alignmentMode
        )
    }

    // toleranceMarginPx is the distance that is still counted as "inside" the pattern.
    fun evaluateStrokes(
        userStrokes: List<List<Offset>>,
        patternStrokes: List<List<Offset>>,
        canvasSize: IntSize,
        patternThicknessPx: Float = 20f,
        userThicknessPx: Float = 20f,
        toleranceMarginPx: Int = 10,
        maxEvaluationDimension: Int = 500,
        alignmentMode: AlignmentMode = AlignmentMode.NONE
    ): AreaComparisonResult {
        if (canvasSize.width <= 0 || canvasSize.height <= 0 || userStrokes.isEmpty() || patternStrokes.isEmpty()) {
            return emptyResult()
        }

        val alignedPattern = alignPattern(patternStrokes, userStrokes, alignmentMode)

        val maxDim = max(canvasSize.width, canvasSize.height)
        val scale = if (maxDim > maxEvaluationDimension) maxEvaluationDimension.toFloat() / maxDim else 1.0f

        val evalWidth = (canvasSize.width * scale).toInt().coerceAtLeast(1)
        val evalHeight = (canvasSize.height * scale).toInt().coerceAtLeast(1)

        val patternThickness = (patternThicknessPx * scale).coerceAtLeast(1f)
        val userThickness = (userThicknessPx * scale).coerceAtLeast(1f)
        val tolerance = (toleranceMarginPx * scale).coerceAtLeast(0f)

        val patternMask = renderMask(alignedPattern, scale, evalWidth, evalHeight, patternThickness)
        val patternTolMask = if (tolerance > 0f)
            renderMask(alignedPattern, scale, evalWidth, evalHeight, patternThickness + 2f * tolerance)
        else patternMask

        val userMask = renderMask(userStrokes, scale, evalWidth, evalHeight, userThickness)
        val userTolMask = if (tolerance > 0f)
            renderMask(userStrokes, scale, evalWidth, evalHeight, userThickness + 2f * tolerance)
        else userMask

        return compareMasks(patternMask, patternTolMask, userMask, userTolMask)
    }

    fun evaluateBitmaps(
        patternBitmap: Bitmap,
        userDrawingBitmap: Bitmap,
        toleranceMarginPx: Int = 10
    ): AreaComparisonResult {
        val width = patternBitmap.width
        val height = patternBitmap.height

        if (width <= 0 || height <= 0 || userDrawingBitmap.width != width || userDrawingBitmap.height != height) {
            return emptyResult()
        }

        val patternMask = maskFromBitmap(patternBitmap)
        val userMask = maskFromBitmap(userDrawingBitmap)

        val patternTol = if (toleranceMarginPx > 0) dilateMask(patternMask, width, height, toleranceMarginPx) else patternMask
        val userTol = if (toleranceMarginPx > 0) dilateMask(userMask, width, height, toleranceMarginPx) else userMask

        return compareMasks(patternMask, patternTol, userMask, userTol)
    }

    private fun compareMasks(
        patternMask: BooleanArray,
        patternTolMask: BooleanArray,
        userMask: BooleanArray,
        userTolMask: BooleanArray
    ): AreaComparisonResult {
        var totalPatternArea = 0
        var totalUserDrawnArea = 0
        var coveredPatternArea = 0
        var outsideUserDrawnArea = 0

        for (i in patternMask.indices) {
            if (patternMask[i]) {
                totalPatternArea++
                if (userTolMask[i]) coveredPatternArea++
            }
            if (userMask[i]) {
                totalUserDrawnArea++
                if (!patternTolMask[i]) outsideUserDrawnArea++
            }
        }

        if (totalPatternArea == 0 || totalUserDrawnArea == 0) {
            return AreaComparisonResult(
                patternCoveragePercent = 0f,
                drawingOutsidePercent = 0f,
                drawingPrecisionPercent = 0f,
                totalPatternAreaPx = totalPatternArea,
                totalUserDrawnAreaPx = totalUserDrawnArea,
                coveredPatternAreaPx = 0,
                outsideDrawnAreaPx = totalUserDrawnArea
            )
        }

        val coveragePercent = (coveredPatternArea.toFloat() / totalPatternArea * 100f).coerceIn(0f, 100f)
        val outsidePercent = (outsideUserDrawnArea.toFloat() / totalUserDrawnArea * 100f).coerceIn(0f, 100f)

        return AreaComparisonResult(
            patternCoveragePercent = coveragePercent,
            drawingOutsidePercent = outsidePercent,
            drawingPrecisionPercent = (100f - outsidePercent).coerceIn(0f, 100f),
            totalPatternAreaPx = totalPatternArea,
            totalUserDrawnAreaPx = totalUserDrawnArea,
            coveredPatternAreaPx = coveredPatternArea,
            outsideDrawnAreaPx = outsideUserDrawnArea
        )
    }

    private fun renderMask(
        strokes: List<List<Offset>>,
        scale: Float,
        width: Int,
        height: Int,
        strokeWidth: Float
    ): BooleanArray {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        try {
            renderStrokesToCanvas(Canvas(bitmap), strokes, scale, strokeWidth, Color.BLACK)
            return maskFromBitmap(bitmap)
        } finally {
            bitmap.recycle()
        }
    }

    private fun maskFromBitmap(bitmap: Bitmap): BooleanArray {
        val w = bitmap.width
        val h = bitmap.height
        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
        val mask = BooleanArray(w * h)
        for (i in pixels.indices) {
            mask[i] = ((pixels[i] ushr 24) and 0xFF) > ALPHA_THRESHOLD
        }
        return mask
    }

    private fun renderStrokesToCanvas(
        canvas: Canvas,
        strokes: List<List<Offset>>,
        scale: Float,
        strokeWidth: Float,
        color: Int
    ) {
        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }
        val fillPaint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        strokes.forEach { stroke ->
            if (stroke.size == 1) {
                val p = stroke.first()
                canvas.drawCircle(p.x * scale, p.y * scale, strokeWidth / 2f, fillPaint)
            } else if (stroke.size > 1) {
                val path = Path()
                path.moveTo(stroke.first().x * scale, stroke.first().y * scale)
                for (i in 1 until stroke.size) {
                    path.lineTo(stroke[i].x * scale, stroke[i].y * scale)
                }
                canvas.drawPath(path, paint)
            }
        }
    }

    private fun dilateMask(mask: BooleanArray, width: Int, height: Int, radius: Int): BooleanArray {
        val dilated = BooleanArray(mask.size)
        val spans = IntArray(2 * radius + 1) { i ->
            val dy = i - radius
            sqrt((radius * radius - dy * dy).toFloat()).toInt()
        }
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (!mask[y * width + x]) continue
                for (dy in -radius..radius) {
                    val ny = y + dy
                    if (ny < 0 || ny >= height) continue
                    val dx = spans[dy + radius]
                    val from = ny * width + (x - dx).coerceAtLeast(0)
                    val to = ny * width + (x + dx).coerceAtMost(width - 1) + 1
                    Arrays.fill(dilated, from, to, true)
                }
            }
        }
        return dilated
    }

    // ---------- optional alignment ----------

    private class Box(val minX: Float, val minY: Float, val maxX: Float, val maxY: Float) {
        val w get() = maxX - minX
        val h get() = maxY - minY
        val cx get() = (minX + maxX) / 2f
        val cy get() = (minY + maxY) / 2f
    }

    private fun boundsOf(strokes: List<List<Offset>>): Box? {
        var minX = Float.MAX_VALUE; var minY = Float.MAX_VALUE
        var maxX = -Float.MAX_VALUE; var maxY = -Float.MAX_VALUE
        var any = false
        strokes.forEach { s -> s.forEach { p ->
            any = true
            minX = min(minX, p.x); minY = min(minY, p.y)
            maxX = max(maxX, p.x); maxY = max(maxY, p.y)
        } }
        return if (any) Box(minX, minY, maxX, maxY) else null
    }

    private fun alignPattern(
        pattern: List<List<Offset>>,
        user: List<List<Offset>>,
        mode: AlignmentMode
    ): List<List<Offset>> {
        if (mode == AlignmentMode.NONE) return pattern
        val pb = boundsOf(pattern) ?: return pattern
        val ub = boundsOf(user) ?: return pattern

        val eps = 1e-3f
        val s = if (mode == AlignmentMode.TRANSLATE_AND_SCALE) {
            val sx = if (pb.w > eps && ub.w > eps) ub.w / pb.w else null
            val sy = if (pb.h > eps && ub.h > eps) ub.h / pb.h else null
            when {
                sx != null && sy != null -> min(sx, sy)
                sx != null -> sx
                sy != null -> sy
                else -> 1f
            }
        } else 1f

        return pattern.map { stroke ->
            stroke.map { p ->
                Offset((p.x - pb.cx) * s + ub.cx, (p.y - pb.cy) * s + ub.cy)
            }
        }
    }

    private fun emptyResult() = AreaComparisonResult(
        patternCoveragePercent = 0f,
        drawingOutsidePercent = 0f,
        drawingPrecisionPercent = 100f,
        totalPatternAreaPx = 0,
        totalUserDrawnAreaPx = 0,
        coveredPatternAreaPx = 0,
        outsideDrawnAreaPx = 0
    )
}

// Maps the pattern into canvas pixels with optional random scale/rotation and smoothing.
fun PatternDrawing.toCanvasPixelStrokes(
    canvasSize: IntSize,
    density: Float = 1f,
    previewWidthFraction: Float = 0.8f,
    previewHeightFraction: Float = 0.7f,
    scaleMultiplier: Float = 1f,
    rotationDegrees: Float = 0f,
    smoothing: Boolean = false
): List<List<Offset>> {
    val bounds = calculateNormalizedBounds(strokes) ?: return emptyList()
    if (canvasSize.width <= 0 || canvasSize.height <= 0) return emptyList()

    val previewWidth = canvasSize.width * previewWidthFraction
    val previewHeight = canvasSize.height * previewHeightFraction
    val previewOriginX = (canvasSize.width - previewWidth) / 2f
    val previewOriginY = (canvasSize.height - previewHeight) / 2f

    val padding = 10f * density
    val availableWidth = (previewWidth - 2f * padding).coerceAtLeast(1f)
    val availableHeight = (previewHeight - 2f * padding).coerceAtLeast(1f)

    val radians = Math.toRadians(rotationDegrees.toDouble())
    val cos = kotlin.math.abs(kotlin.math.cos(radians)).toFloat()
    val sin = kotlin.math.abs(kotlin.math.sin(radians)).toFloat()

    val effW = bounds.width.coerceAtLeast(0.001f)
    val effH = bounds.height.coerceAtLeast(0.001f)
    val rotatedW = (effW * cos + effH * sin).coerceAtLeast(0.001f)
    val rotatedH = (effW * sin + effH * cos).coerceAtLeast(0.001f)

    val scale = minOf(availableWidth / rotatedW, availableHeight / rotatedH) * scaleMultiplier

    val offsetX = (previewWidth - bounds.width * scale) / 2f
    val offsetY = (previewHeight - bounds.height * scale) / 2f

    val pivotX = previewWidth / 2f
    val pivotY = previewHeight / 2f
    val rotCos = kotlin.math.cos(radians).toFloat()
    val rotSin = kotlin.math.sin(radians).toFloat()

    return strokes.map { stroke ->
        val local = stroke.map { point ->
            Offset(
                x = (point.x - bounds.minX) * scale + offsetX,
                y = (point.y - bounds.minY) * scale + offsetY
            )
        }
        val shaped = if (smoothing) chaikinSmooth(local, iterations = 2) else local
        shaped.map { p ->
            val dx = p.x - pivotX
            val dy = p.y - pivotY
            Offset(
                x = previewOriginX + pivotX + dx * rotCos - dy * rotSin,
                y = previewOriginY + pivotY + dx * rotSin + dy * rotCos
            )
        }
    }
}
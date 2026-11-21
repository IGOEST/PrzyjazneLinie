
package com.example.friendlylines.child_app.reports

import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.PatternResult
import java.util.Locale

object ReportBuilder {

    private fun decimal(value: Float?): String =
        value?.let { String.format(Locale.US, "%.4f", it) } ?: "N/A"

    private fun decimal(value: Double): String =
        String.format(Locale.US, "%.2f", value)

    /**
     * PDF
     */
    fun buildPdfReport(
        learningStepResult: LearningStepResult,
        patterns: List<PatternResult>
    ): String = buildString {

        fun section(title: String) {
            appendLine()
            appendLine(title)
            appendLine()
        }

        fun field(name: String, value: Any?) {
            appendLine("$name: ${value ?: "N/A"}")
        }

        appendLine("FRIENDLY LINES - REPORT")
        appendLine()

        section("PARAMETERS EXPLANATION")
        appendLine("shapeMatch - percentage of shape similarity")
        appendLine("insideArea - percentage of drawing inside the pattern area")
        appendLine("outsideArea - percentage of drawing outside the pattern area")
        appendLine("shapeDistance - distance value returned by ShapeCorrectness")
        appendLine("shapeIsValid - whether the shape passed validity checks")
        appendLine("timeSeconds - duration of an attempt in seconds")
        appendLine("isSuccessful - whether the attempt was successful")
        appendLine("Coordinates in the trajectory CSV are normalized to the 0-1 range")
        appendLine("Each stroke represents a separate finger movement")

        section("1. LEARNING STEP")

        field("Name", learningStepResult.name)
        field("Correct coverage", "${learningStepResult.correctCoverage}%")
        field("Line out of bounds", "${learningStepResult.lineOutOfBounds}%")
        field("Shape match", "${learningStepResult.shapeMatch}%")
        field("Show starting point", learningStepResult.showStartingPoint)
        field("Test mode", learningStepResult.testMode)
        field("Time limit", "${learningStepResult.timeLimit} s")
        field("Attempts", learningStepResult.attempts)
        field("Random pattern order", learningStepResult.randomPatternOrder)

        section("2. PATTERN RESULTS")

        field("Number of patterns", patterns.size)

        patterns.forEachIndexed { index, pattern ->

            section("PATTERN ${index + 1}: ${pattern.patternName}")

            appendLine("Configuration")
            field("Pattern color", pattern.config.patternColor.key)
            field("Trace color", pattern.config.traceColor.key)
            field("Background color", pattern.config.backgroundColor.key)
            field("Thickness", pattern.config.patternThickness)
            field("Thickness dp", pattern.config.patternThicknessDp.value)
            field("Pattern variety", pattern.config.patternVariety)
            field("Order", pattern.config.order)

            appendLine()
            appendLine("Results")

            field("Shape match", "${pattern.shapeMatch}%")
            field("Inside area", "${pattern.insideArea}%")
            field("Outside area", "${pattern.outsideArea}%")
            field("Correct starting point", pattern.correctStartingPoint)
            field("Attempts", pattern.attempts)
            field("Last attempt time", "${decimal(pattern.lastAttemptTime)} s")
            field("Total time", "${decimal(pattern.totalTime)} s")

            appendLine()
            appendLine("Individual attempts")

            pattern.attemptResults.forEach { attempt ->

                appendLine()
                appendLine("ATTEMPT ${attempt.attemptNumber}")
                appendLine()

                field("Shape match", "${attempt.shapeMatch}%")
                field("Inside area", "${attempt.insideArea}%")
                field("Outside area", "${attempt.outsideArea}%")
                field("Time", "${decimal(attempt.timeSeconds)} s")
                field("Successful", attempt.isSuccessful)
                field("Shape distance", decimal(attempt.shapeDistance))
                field("Shape is valid", attempt.shapeIsValid)
            }
        }
    }

    /**
     * CSV
     */
    fun buildTrajectoryCsv(
        patterns: List<PatternResult>
    ): String = buildString {

        fun csv(value: Any?): String {
            val text = value?.toString() ?: ""
            return "\"${text.replace("\"", "\"\"")}\""
        }

        // column names
        appendLine(
            listOf(
                "patternId",
                "patternName",
                "attemptNumber",
                "attemptTimeSeconds",
                "isSuccessful",
                "strokeNumber",
                "pointNumber",
                "x",
                "y",
                "timeReached"
            ).joinToString(",")
        )

        patterns.forEach { pattern ->
            pattern.attemptResults.forEach { attempt ->

                attempt.trajectory.forEachIndexed { strokeIndex, stroke ->

                    stroke.forEachIndexed { pointIndex, timedPoint ->

                        appendLine(
                            listOf(
                                csv(pattern.patternId),
                                csv(pattern.patternName),
                                csv(attempt.attemptNumber),
                                csv(decimal(attempt.timeSeconds)),
                                csv(attempt.isSuccessful),
                                csv(strokeIndex + 1),
                                csv(pointIndex + 1),
                                csv(decimal(timedPoint.point.x)),
                                csv(decimal(timedPoint.point.y)),
                                csv(decimal(timedPoint.timeReached))
                            ).joinToString(",")
                        )
                    }
                }
            }
        }
    }
}

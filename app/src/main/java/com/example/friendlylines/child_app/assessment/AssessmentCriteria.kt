package com.example.friendlylines.child_app.assessment

import com.example.shared.data.drafts.AccuracyLevel

/**
 * Decides whether an attempt is successful. Thresholds depend on the accuracy level.
 */
object AssessmentCriteria {

    private fun thresholdFor(level: AccuracyLevel): Int {
        return when (level) {
            AccuracyLevel.EASY -> 70
            AccuracyLevel.MEDIUM -> 80
            AccuracyLevel.HARD -> 90
        }
    }

    fun areaScore(coverage: Int, outside: Int): Double {
        return (coverage + (100 - outside)) / 2.0
    }
    fun isSuccessful(accuracyLevel: AccuracyLevel, shapeMatch: Int, coverage: Int, outside: Int): Boolean {
        return areaScore(coverage, outside) >= thresholdFor(accuracyLevel)
    }
}
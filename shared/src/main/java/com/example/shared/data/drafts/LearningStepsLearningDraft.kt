package com.example.shared.data.drafts

enum class AccuracyLevel {
    EASY,
    MEDIUM,
    HARD
}

data class LearningStepsLearningDraft(
    val repetitions: Int = 3,
    val attempts: Int = 3,
    val timeLimit: Int = 30,
    val accuracyLevel: AccuracyLevel = AccuracyLevel.MEDIUM,
    val startingPointEnabled: Boolean = false,
    val randomPatternOrder: Boolean = false,
)
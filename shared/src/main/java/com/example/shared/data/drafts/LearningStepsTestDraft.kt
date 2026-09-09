package com.example.shared.data.drafts

data class LearningStepsTestDraft(
    val id: Long,
    val repetitions: Int = 3,
    val attempts: Int = 1,
    val timeLimit: Int = 30,
    val accuracyLevel: AccuracyLevel = AccuracyLevel.MEDIUM,
    val startingPointEnabled: Boolean = false,
    val randomPatternOrder: Boolean = false
)
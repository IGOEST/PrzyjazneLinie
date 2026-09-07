package com.example.shared.data.drafts

data class LearningStepsDraft(
    val id: Long,
    val name: String = "",

    // learning settings
    val repetitions: Int = 3,
    val attempts: Int = 3,
    val timeLimit: Int = 30,
    val accuracyLevel: AccuracyLevel = AccuracyLevel.MEDIUM,
    val startingPointEnabled: Boolean = false,
    val randomPatternOrder: Boolean = false,

    val patterns: List<LearningStepsPatternConfigDraft> = emptyList(),
)

enum class AccuracyLevel {
    EASY,
    MEDIUM,
    HARD
}
package com.example.shared.data.drafts

data class LearningStepsDraft(
    val id: Long,
    val name: String = "",
    val isActive: Boolean = false,
    val isLearning: Boolean = true,
    val isTest: Boolean = false,
    val isExample: Boolean = false,

    val patterns: LearningStepsPatternsDraft,
    val learning: LearningStepsLearningDraft,
    val test: LearningStepsTestDraft,
    val reinforcements: LearningStepsReinforcementsDraft
)
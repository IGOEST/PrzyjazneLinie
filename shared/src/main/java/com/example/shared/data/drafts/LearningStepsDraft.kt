package com.example.shared.data.drafts

data class LearningStepsDraft(
    val id: Long,
    val name: String = "",
    val patterns: List<LearningStepsPatternConfigDraft> = emptyList(),
)
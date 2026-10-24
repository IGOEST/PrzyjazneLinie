package com.example.shared.data.drafts

data class LearningStepsPatternsDraft(
    val patterns: List<LearningStepsPatternConfigDraft> = emptyList(),
    val scrollToConfigId: Long? = null
)
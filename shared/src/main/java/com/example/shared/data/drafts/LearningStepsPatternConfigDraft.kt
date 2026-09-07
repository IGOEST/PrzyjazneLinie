package com.example.shared.data.drafts

import com.example.shared.data.models.PatternItem

data class LearningStepsPatternConfigDraft(
    val id: Long,
    val pattern: PatternItem,
    val width: PatternWidth = PatternWidth.MEDIUM,
    val patternColor: ColorOption? = null,
    val writingColor: ColorOption? = null,
    val backgroundColor: ColorOption? = null,
    val patternVariety: Boolean = false,
    val order: Int,
    val isEnabled: Boolean = true
)
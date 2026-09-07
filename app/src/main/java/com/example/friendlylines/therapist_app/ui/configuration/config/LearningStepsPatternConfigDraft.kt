package com.example.friendlylines.therapist_app.ui.configuration.config

import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem

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
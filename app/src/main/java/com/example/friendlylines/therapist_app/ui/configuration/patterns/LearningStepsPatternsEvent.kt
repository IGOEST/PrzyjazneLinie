package com.example.friendlylines.therapist_app.ui.configuration.patterns

import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.models.PatternItem

sealed class LearningStepsPatternsEvent {
    data class SetPatternEnabled(val configId: Long, val enabled: Boolean) : LearningStepsPatternsEvent()
    data class MovePattern(val configId: Long, val direction: MoveDirection) : LearningStepsPatternsEvent()
    data class DeletePattern(val configId: Long) : LearningStepsPatternsEvent()
    data class CopyPattern(val configId: Long) : LearningStepsPatternsEvent()
    data class AddPattern(
        val pattern: PatternItem,
        val width: PatternWidth,
        val patternColor: ColorOption?,
        val writingColor: ColorOption?,
        val backgroundColor: ColorOption?,
        val patternVariety: Boolean
    ) : LearningStepsPatternsEvent()
    data class UpdatePattern(
        val configId: Long,
        val pattern: PatternItem,
        val width: PatternWidth,
        val patternColor: ColorOption?,
        val writingColor: ColorOption?,
        val backgroundColor: ColorOption?,
        val patternVariety: Boolean
    ) : LearningStepsPatternsEvent()
    data object ScrollToConfigHandled : LearningStepsPatternsEvent()
}
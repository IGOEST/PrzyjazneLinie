package com.example.friendlylines.child_app.ui

import androidx.compose.ui.unit.Dp
import com.example.shared.data.drafts.ColorOption

data class PatternConfig(
    val patternColor: ColorOption,
    val traceColor: ColorOption,
    val backgroundColor: ColorOption,
    val patternThickness: String,
    val patternThicknessDp: Dp,
    val smoothingEnabled: Boolean,
    val showStartingPoint: Boolean,
    val testMode: Boolean,
    val timeLimit: Int,
    val attempts: Int
)

data class LearningStepResult(
    val name: String,
    val correctCoverage: Int = 0,
    val lineOutOfBounds: Int = 0,
    val shapeMatch: Int = 0,
)

data class PatternResult(
    val patternId: Long,
    val patternName: String,
    val config: PatternConfig,
    val correctCoverage: Int = 0,
    val lineOutOfBounds: Int = 0,
    val shapeMatch: Int = 0,
    val correctStartingPoint: Boolean = true
)
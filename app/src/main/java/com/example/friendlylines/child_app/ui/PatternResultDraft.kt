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
    val patternVariety: Boolean
)

data class LearningStepResult(
    val name: String,
    val correctCoverage: Int = 0,
    val lineOutOfBounds: Int = 0,
    val shapeMatch: Int = 0,
    val showStartingPoint: Boolean,
    val testMode: Boolean,
    val timeLimit: Int,
    val attempts: Int,
    val randomPatternOrder: Boolean,
    val exampleKey: String? = null,
    val praiseWords: Set<String>,
    val praiseWordsEnabled: Boolean,
    val animations: Set<String>,
    val animationsEnabled: Boolean,
    val endFanfare: Boolean,
    val endAnimation: Boolean
)

data class PatternResult(
    val patternId: Long,
    var patternName: String,
    var originalName: String,
    val config: PatternConfig,
    val correctCoverage: Int = 0,
    val lineOutOfBounds: Int = 0,
    val shapeMatch: Int = 0,
    val correctStartingPoint: Boolean = true,
    var lastAttemptTime: Double = 0.0,
    var totalTime: Double = 0.0,
    var attempts: Int,
    val exampleKey: String? = null,
    var displayNumber: Int = 1
)
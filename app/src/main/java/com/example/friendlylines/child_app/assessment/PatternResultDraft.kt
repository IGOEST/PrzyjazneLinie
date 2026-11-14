package com.example.friendlylines.child_app.ui

import androidx.compose.ui.unit.Dp
import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.models.Point

data class PatternConfig(
    val patternColor: ColorOption,
    val traceColor: ColorOption,
    val backgroundColor: ColorOption,
    val patternThickness: String,
    val patternThicknessDp: Dp,
    val smoothingEnabled: Boolean,
    val patternVariety: Boolean,
    val order: Int
)

data class LearningStepResult(
    val name: String,
    var correctCoverage: Int = 0,
    var lineOutOfBounds: Int = 0,
    var shapeMatch: Int = 0,
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
    val endAnimation: Boolean,
    val isExample: Boolean = false,
)

data class PatternResult(
    val patternId: Long,
    var patternName: String,
    var originalName: String,
    val config: PatternConfig,
    var shapeMatch: Int = 0,
    val correctStartingPoint: Boolean = true,
    var lastAttemptTime: Double = 0.0,
    var totalTime: Double = 0.0,
    var attempts: Int,
    val exampleKey: String? = null,
    var displayNumber: Int = 1,
    var insideArea: Int = 0,
    var outsideArea: Int = 0,
    var attemptResults: List<AttemptResult> = emptyList()
)

data class AttemptResult(
    val attemptNumber: Int,
    val shapeMatch: Int,
    val insideArea: Int,
    val outsideArea: Int,
    val timeSeconds: Double,
    val isSuccessful: Boolean,
    val trajectory: List<List<Point>> = emptyList(),
    val shapeDistance: Float? = null,
    val shapeIsValid: Boolean? = null
)
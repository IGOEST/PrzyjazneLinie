package com.example.friendlylines.child_app.ui

data class PatternConfig(
    val patternColor: String,
    val traceColor: String,
    val backgroundColor: String,
    val patternThickness: String,
    val showStartingPoint: Boolean,
    val testMode: Boolean,
    val timeLimit: Int
)

data class LearningStepResult(
    val correctCoverage: Int,
    val lineOutOfBounds: Int,
    val shapeMatch: Int,
)

data class PatternResult(
    val patternName: String,
    val config: PatternConfig,
    val correctCoverage: Int,
    val lineOutOfBounds: Int,
    val shapeMatch: Int,
    val correctStartingPoint: Boolean
)
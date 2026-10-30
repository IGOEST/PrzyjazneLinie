package com.example.friendlylines.child_app.main

import androidx.compose.ui.geometry.Offset
import com.example.friendlylines.child_app.assessment.ShapeAssessmentResult
import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.PatternResult
import com.example.shared.data.models.PatternDrawing

data class GameState(
    val screenState: String = "info",
    val learningStepResult: LearningStepResult? = null,
    val shapeAssessmentResult: ShapeAssessmentResult? = null,
    val patternResults: List<PatternResult> = emptyList(),
    val currentPattern: Int = 0,
    val currentPatternDrawing: PatternDrawing? = null,
    val userStrokes: List<List<Offset>> = emptyList(),
    val isMenuOpen: Boolean = false,
    val activeStepName: String = "",
    val activeStepLearning: Boolean = false,
    val activeStepTest: Boolean = false,
    val patternScale: Float = 1f,
    val patternRotation: Float = 0f,
)
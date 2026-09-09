package com.example.friendlylines.therapist_app.ui.materials.create_new

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.friendlylines.therapist_app.ui.materials.models.DrawingStroke

data class CreatePatternScreenState(
    val smoothingEnabled: Boolean = false,
    val straightLineEnabled: Boolean = false,
    val showSaveDialog: Boolean = false,
    val patternName: String = "",
    val patternNameError: PatternNameError? = null,
    val showExitDialog: Boolean = false,
    val exitDestination: ExitDestination? = null,
    val savedPatternId: Long? = null
)
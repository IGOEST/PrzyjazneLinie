package com.example.friendlylines.therapist_app.ui.materials.create_new

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.friendlylines.therapist_app.ui.materials.models.DrawingStroke
import com.example.shared.data.models.Point

sealed class CreatePatternScreenEvent {
    data class SmoothingEnabledChanged(val enabled: Boolean) : CreatePatternScreenEvent()
    data class StraightLineEnabledChanged(val enabled: Boolean) : CreatePatternScreenEvent()
    data object ClearDrawingClicked : CreatePatternScreenEvent()
    data object SaveClicked : CreatePatternScreenEvent()
    data class PatternNameChanged(val name: String) : CreatePatternScreenEvent()
    data object PatternBlankName : CreatePatternScreenEvent()
    data object PatternNameExists : CreatePatternScreenEvent()
    data object SaveDismissed : CreatePatternScreenEvent()
    data class SaveConfirmed(val strokes: List<List<Point>>, val isComplex: Boolean, val createdAt: Long) : CreatePatternScreenEvent()
    data object SaveSuccessHandled : CreatePatternScreenEvent()
    data object ExitRequested : CreatePatternScreenEvent()
    data class ExitToDestination(val destination: ExitDestination) : CreatePatternScreenEvent()
    data object ExitConfirmed : CreatePatternScreenEvent()
    data object ExitDismissed : CreatePatternScreenEvent()
    data object ExitNavigationHandled : CreatePatternScreenEvent()
}
package com.example.friendlylines.child_app.main

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

sealed class ChildMainEvent {
    object GoToNextScreen : ChildMainEvent()
    object BackToMainMenu : ChildMainEvent()
    data class OnCanvasSizeChanged(val size: IntSize) : ChildMainEvent()
    data class OnPointerDown(val position: Offset) : ChildMainEvent()
    data class OnPointerMove(val position: Offset) : ChildMainEvent()
    object OnPointerUp : ChildMainEvent()
    object ToggleMenu : ChildMainEvent()
}

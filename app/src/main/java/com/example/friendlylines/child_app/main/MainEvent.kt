package com.example.friendlylines.child_app.main

import androidx.compose.ui.geometry.Offset

sealed class ChildMainEvent {
    object GoToNextScreen : ChildMainEvent()
    object BackToMainMenu : ChildMainEvent()
    data class OnPointerDown(val position: Offset) : ChildMainEvent()
    data class OnPointerMove(val position: Offset) : ChildMainEvent()
    object OnPointerUp : ChildMainEvent()
    object ToggleMenu : ChildMainEvent()
}

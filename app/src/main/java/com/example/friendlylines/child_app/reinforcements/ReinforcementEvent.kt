package com.example.friendlylines.child_app.reinforcements

sealed class ReinforcementEvent {
    data class PlayPraise(val word: String) : ReinforcementEvent()
    data class PlayAnimation(val theme: String) : ReinforcementEvent()
}
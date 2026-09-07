package com.example.friendlylines.child_app.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChildViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    fun onEvent(event: ChildMainEvent) {
        _state.update { currentState ->
            reduce(currentState, event)
        }
    }

    private fun reduce(state: GameState, event: ChildMainEvent): GameState {
        return when (event) {
            is ChildMainEvent.GoToNextScreen -> state.copy(
                screenState = when (state.screenState) {
                    "info" -> "main"
                    "main" -> "game"
                    "game" -> "end"
                    "end" -> "main"
                    else -> "info"
                }
            )
        }
    }
}
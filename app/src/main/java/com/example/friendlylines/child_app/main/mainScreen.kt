package com.example.friendlylines.child_app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun ChildMainScreen(viewModel: ChildViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (state.screenState) {
        "info" -> {
            LaunchedEffect(Unit) {
                delay(5000L)
                viewModel.onEvent(ChildMainEvent.GoToNextScreen)
            }
            InformationScreen()
        }
        "main" -> {
            MainMenuScreen("Do Dodania",
                "Do Dodania",
                onPlayClick = { viewModel.onEvent(ChildMainEvent.GoToNextScreen)}
            )
        }
        "game" -> {
        }
        "end" -> {
        }
    }
}

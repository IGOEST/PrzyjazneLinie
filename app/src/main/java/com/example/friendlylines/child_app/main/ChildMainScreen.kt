package com.example.friendlylines.child_app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.child_app.ui.GameDrawingScreen
import com.example.friendlylines.child_app.ui.InfoSplashScreen
import com.example.friendlylines.child_app.ui.MainMenuScreen
import com.example.friendlylines.child_app.ui.ResultsScreen
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
            InfoSplashScreen(
                appTitle = stringResource(R.string.app_name),
                appIconRes = R.drawable.app_icon,
                onContinue = { viewModel.onEvent(ChildMainEvent.GoToNextScreen) }
            )
        }
        "main" -> {
            MainMenuScreen(
                "Do Dodania",
                "Do Dodania",
                onPlayClick = { viewModel.onEvent(ChildMainEvent.GoToNextScreen) }
            )
        }
        "game" -> {
            GameDrawingScreen(
                onBackClick = { viewModel.onEvent(ChildMainEvent.BackToMainMenu) }
            )
        }
        "end" -> {
            val lsResult = state.learningStepResult
            if (lsResult != null && state.patternResults.isNotEmpty()) {
                ResultsScreen(
                    onPlayClick = { viewModel.onEvent(ChildMainEvent.GoToNextScreen) },
                    learningStepResult = lsResult,
                    patterns = state.patternResults
                )
            }
        }
    }
}

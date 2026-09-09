package com.example.friendlylines.child_app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.child_app.ui.GameDrawingScreen
import com.example.friendlylines.child_app.ui.InformationScreen
import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.MainMenuScreen
import com.example.friendlylines.child_app.ui.PatternConfig
import com.example.friendlylines.child_app.ui.PatternResult
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
            InformationScreen()
        }
        "main" -> {
            MainMenuScreen(
                "Do Dodania",
                "Do Dodania",
                onPlayClick = { viewModel.onEvent(ChildMainEvent.GoToNextScreen) }
            )
        }
        "game" -> {
            LaunchedEffect(Unit) {
                delay(5000L)
                viewModel.onEvent(ChildMainEvent.GoToNextScreen)
            }
            GameDrawingScreen(
                onBackClick = { viewModel.onEvent(ChildMainEvent.BackToMainMenu) }
            )
        }
        "end" -> {
            ResultsScreen(
                onPlayClick = { viewModel.onEvent(ChildMainEvent.GoToNextScreen) },
                learningStepResult = learningStep,
                patterns = patterns
            )
        }
    }
}

//DO TESTU
val learningStep = LearningStepResult(
    correctCoverage = 90,
    lineOutOfBounds = 12,
    shapeMatch = 80
)

val patterns = listOf(
    PatternResult(
        patternName = "Wzór 1 (Linia Pozioma)",
        config = PatternConfig(
            patternColor = "Pomarańczowy",
            traceColor = "Pomarańczowy",
            backgroundColor = "Pomarańczowy",
            patternThickness = "Gruby",
            showStartingPoint = false,
            testMode = true,
            timeLimit = 10
        ),
        correctCoverage = 90,
        lineOutOfBounds = 1,
        shapeMatch = 88,
        correctStartingPoint = true
    ),
    PatternResult(
        patternName = "Wzór 2 (Linia Pionowa)",
        config = PatternConfig(
            patternColor = "Zielony",
            traceColor = "Zielony",
            backgroundColor = "Zielony",
            patternThickness = "Cienki",
            showStartingPoint = true,
            testMode = false,
            timeLimit = 15
        ),
        correctCoverage = 78,
        lineOutOfBounds = 20,
        shapeMatch = 75,
        correctStartingPoint = false
    )
)

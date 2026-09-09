package com.example.friendlylines.child_app.main

import androidx.appcompat.R
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.PatternConfig
import com.example.friendlylines.child_app.ui.PatternResult
import com.example.shared.data.drafts.PatternConfigOptions
import com.example.shared.data.drafts.PatternConfigOptions.toDp
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.models.toPatternDrawing
import com.example.shared.data.repositories.LearningStepRepository
import com.example.shared.data.repositories.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource

@HiltViewModel
class ChildViewModel  @Inject constructor(
    private val learningStepRepository: LearningStepRepository,
    private val patternRepository: PatternRepository
)  : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var gameTimerJob: Job? = null
    private var finishTimerJob: Job? = null

    private var timeLimitRemainingMillis: Long? = null
    private var timerStartedAt: Long = 0L
    private var isFinishTimerActive = false

    init {
        loadActiveStepData()
    }

    fun onEvent(event: ChildMainEvent) {
        when (event) {
            is ChildMainEvent.OnPointerDown -> handlePointerDown(event.position)
            is ChildMainEvent.OnPointerMove -> handlePointerMove(event.position)
            is ChildMainEvent.OnPointerUp -> handlePointerUp()
            is ChildMainEvent.ToggleMenu -> handleToggleMenu()
            is ChildMainEvent.BackToMainMenu -> handleBackToMainMenu()
            else -> _state.update { currentState ->
                reduce(currentState, event)
            }
        }
    }

    private fun handlePointerDown(position: Offset) {
        finishTimerJob?.cancel()
        _state.update { currentState ->
            val newStrokes = currentState.userStrokes + listOf(listOf(position))
            currentState.copy(userStrokes = newStrokes)
        }
    }

    private fun handlePointerMove(position: Offset) {
        _state.update { currentState ->
            if (currentState.userStrokes.isEmpty()) return@update currentState
            
            val lastStroke = currentState.userStrokes.last()
            val updatedLastStroke = lastStroke + position
            val newStrokes = currentState.userStrokes.dropLast(1) + listOf(updatedLastStroke)
            
            currentState.copy(userStrokes = newStrokes)
        }
    }

    private fun handlePointerUp() {
        startFinishTimer()  // timer after trace is finished, in case user continues drawing
    }

    private fun startFinishTimer() {
        finishTimerJob?.cancel()
        finishTimerJob = viewModelScope.launch {
            delay(1500L)
            goToNextPattern()
        }
    }

    private fun startTimeLimitTimer(limitSeconds: Int) {
        if (limitSeconds <= 0) {
            timeLimitRemainingMillis = null
            return
        }
        gameTimerJob?.cancel()
        timeLimitRemainingMillis = limitSeconds * 1000L
        startTimerWithMillis(timeLimitRemainingMillis!!)
    }

    private fun startTimerWithMillis(millis: Long) {
        timerStartedAt = System.currentTimeMillis()
        gameTimerJob = viewModelScope.launch {
            delay(millis)
            goToNextPattern()
        }
    }

    private fun handleToggleMenu() {
        val currentlyOpen = _state.value.isMenuOpen
        if (!currentlyOpen) {
            // Opening menu - Pause
            val elapsed = System.currentTimeMillis() - timerStartedAt
            timeLimitRemainingMillis = timeLimitRemainingMillis?.let { it - elapsed }

            gameTimerJob?.cancel()

            if (finishTimerJob?.isActive == true) {
                isFinishTimerActive = true
                finishTimerJob?.cancel()
            } else {
                isFinishTimerActive = false
            }
        } else {
            // Closing menu - Resume
            timeLimitRemainingMillis?.let {
                if (it > 0) {
                    startTimerWithMillis(it)
                }
            }

            if (isFinishTimerActive) {
                startFinishTimer()
            }
        }

        _state.update { it.copy(isMenuOpen = !currentlyOpen) }
    }

    private fun handleBackToMainMenu() {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()
        timeLimitRemainingMillis = null
        isFinishTimerActive = false
        _state.update { it.copy(
            screenState = "main",
            userStrokes = emptyList(),
            isMenuOpen = false
        ) }
    }

    private fun goToNextPattern() {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()
        timeLimitRemainingMillis = null
        isFinishTimerActive = false

        _state.update { currentState ->
            val nextIndex = currentState.currentPattern + 1
            if (nextIndex < currentState.patternResults.size) {
                // Clear strokes and load next pattern
                val newState = currentState.copy(
                    currentPattern = nextIndex,
                    userStrokes = emptyList(),
                    currentPatternDrawing = null
                )
                loadPatternDrawing(nextIndex, newState)
                newState
            } else {
                currentState.copy(screenState = "end")
            }
        }
    }

    private fun reduce(state: GameState, event: ChildMainEvent): GameState {
        return when (event) {
            is ChildMainEvent.GoToNextScreen -> {
                val nextScreen = when (state.screenState) {
                    "info" -> "main"
                    "main" -> "game"
                    "game" -> "end"
                    "end" -> "main"
                    else -> "info"
                }
                
                if (nextScreen == "game") {
                    // Reset to first pattern and load it
                    val resetState = state.copy(
                        screenState = nextScreen,
                        currentPattern = 0,
                        userStrokes = emptyList()
                    )
                    loadPatternDrawing(0, resetState)
                    return resetState
                }

                state.copy(screenState = nextScreen)
            }
            else -> state
        }
    }

    private fun loadPatternDrawing(index: Int, state: GameState) {
        val patternResult = state.patternResults.getOrNull(index) ?: return
        
        // Start time limit for the new pattern
        startTimeLimitTimer(patternResult.config.timeLimit)

        viewModelScope.launch {
            try {
                val patternWithStrokes = patternRepository.getPattern(patternResult.patternId)
                val drawing = patternWithStrokes?.toPatternDrawing()
                _state.update { it.copy(currentPatternDrawing = drawing) }
            } catch (_: Exception) {}
        }
    }


    private fun loadActiveStepData() {
        viewModelScope.launch {
            try {
                val step = learningStepRepository.getActiveStep()
                val activePatterns = learningStepRepository.getPatternConfigurations(step.id)
                    .filter { it.isEnabled }
                    .sortedBy { it.order }

                if (activePatterns.isEmpty()) return@launch

                val patternResults = mutableListOf<PatternResult>()
                val repetitions = step.repetitions
                val learningStepResult = LearningStepResult(name = step.name)
                var patternCount = 1
                for (p in activePatterns) {
                    for (i in 0 until repetitions) {
                        val patternEntity = patternRepository.getPattern(p.patternId)?.pattern

                        if (patternEntity != null) {
                            val widthEnum = try {
                                PatternWidth.valueOf(p.width)
                            } catch (_: Exception) {
                                PatternWidth.MEDIUM
                            }

                            val config = PatternConfig(
                                patternColor =
                                    PatternConfigOptions.getPatternAndWritingColor(p.patternColor),
                                traceColor = PatternConfigOptions.getPatternAndWritingColor(p.writingColor),
                                backgroundColor = PatternConfigOptions.getBackgroundColor(p.backgroundColor),
                                patternThickness = p.width,
                                patternThicknessDp = widthEnum.toDp(),
                                smoothingEnabled = patternEntity.smoothingEnabled,
                                showStartingPoint = step.startingPointEnabled,
                                testMode = false,
                                timeLimit = step.timeLimit,
                                attempts = step.attempts
                            )

                            patternResults.add(
                                PatternResult(
                                    patternId = p.patternId,
                                    patternName = patternEntity.name + " " + patternCount,
                                    config = config
                                )
                            )
                            patternCount++
                        }
                    }
                }

                _state.update { it.copy(
                    patternResults = patternResults,
                    learningStepResult = learningStepResult
                ) }
            } catch (_: Exception) {}
        }
    }
}

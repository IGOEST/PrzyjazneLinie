package com.example.friendlylines.child_app.main

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.PatternConfig
import com.example.friendlylines.child_app.ui.PatternResult
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.drafts.toDp
import com.example.shared.data.models.toPatternDrawing
import com.example.shared.data.drafts.PatternConfigOptions
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
import androidx.compose.ui.unit.IntSize
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.repositories.LearningStepsRepository
import com.example.friendlylines.child_app.assessment.ShapeCorrectness
import com.example.friendlylines.therapist_app.ui.materials.geometry.toNormalized

@HiltViewModel
class ChildViewModel  @Inject constructor(
    private val learningStepRepository: LearningStepsRepository,
    private val patternRepository: PatternRepository
)  : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val shapeCorrectness = ShapeCorrectness(
        sampleCount = 32,
        correctThreshold = 0.80f
    )

    private var gameTimerJob: Job? = null
    private var finishTimerJob: Job? = null

    private var timeLimitRemainingMillis: Long? = null
    private var timerStartedAt: Long = 0L
    private var isFinishTimerActive = false
    private var canvasSize: IntSize = IntSize.Zero

    init {
    viewModelScope.launch {
        learningStepRepository.observeActiveStep().collect { step ->
            if (step != null) {
                loadActiveStepData(step)
            }
        }
    }
}

    fun onEvent(event: ChildMainEvent) {
        when (event) {
            is ChildMainEvent.OnPointerDown -> handlePointerDown(event.position)
            is ChildMainEvent.OnPointerMove -> handlePointerMove(event.position)
            is ChildMainEvent.OnPointerUp -> handlePointerUp()
            is ChildMainEvent.OnCanvasSizeChanged -> {canvasSize = event.size}
            is ChildMainEvent.ToggleMenu -> handleToggleMenu()
            is ChildMainEvent.BackToMainMenu -> handleBackToMainMenu()
            else -> _state.update { currentState ->
                reduce(currentState, event)
            }
        }
    }

    // Finger actions functions
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
        val currentState = _state.value
        val referenceDrawing = currentState.currentPatternDrawing

        val result = if (referenceDrawing != null) {
            val userPoints = currentState.userStrokes.map { stroke ->
                stroke.map { point ->
                    Offset(point.x, point.y).toNormalized(canvasSize)
                }
            }

            shapeCorrectness.evaluate(
                userStrokes = userPoints,
                referenceStrokes = referenceDrawing.strokes,
                canvasSize = canvasSize
            )
        } else {
            null
        }
        _state.update {
            it.copy(shapeAssessmentResult = result)
        }

        if (result != null) {
            _state.value.patternResults
                .getOrNull(_state.value.currentPattern)
                ?.let { patternResult ->
                    patternResult.shapeMatch = (result.score * 100).toInt()
                }
        }

        startFinishTimer()  // timer after trace is finished, in case user continues drawing
    }

    // TIMER functions (time limit)
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

    // Time is counted for whole pattern and last (or successful) attempt
    private fun finishAttempt() {
        val limitMillis = (_state.value.learningStepResult?.timeLimit ?: return) * 1000L
        val remaining = (timeLimitRemainingMillis ?: return) -
                (System.currentTimeMillis() - timerStartedAt)
        val s = (limitMillis - remaining).coerceIn(0L, limitMillis) / 1000.0

        val result = _state.value.patternResults
            .getOrNull(_state.value.currentPattern) ?: return
        result.totalTime += s - 1.5   // substract finish timer
        result.lastAttemptTime = s - 1.5
    }

    // TODO: Restarts if pattern is wrong and there are attempts left
    private fun restartAttempt() {
        finishAttempt()
        finishTimerJob?.cancel()
        _state.update { it.copy(userStrokes = emptyList()) }
        //new time limit
        startTimeLimitTimer(_state.value.learningStepResult?.timeLimit ?: 0)
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
        finishAttempt()
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
                    currentPatternDrawing = null,
                    shapeAssessmentResult = null
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
                    val prepared = preparePatterns(
                        state.patternResults.toMutableList(),
                        state.learningStepResult?.testMode == false,
                        state.learningStepResult?.testMode ?: false
                    )
                    val resetState = state.copy(
                        screenState = nextScreen,
                        currentPattern = 0,
                        userStrokes = emptyList(),
                        patternResults = prepared
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
        startTimeLimitTimer(state.learningStepResult?.timeLimit ?: 0)

        viewModelScope.launch {
            try {
                val patternWithStrokes = patternRepository.getPattern(patternResult.patternId)
                val drawing = patternWithStrokes?.toPatternDrawing()
                _state.update { it.copy(currentPatternDrawing = drawing) }
            } catch (_: Exception) {}
        }
    }

    private fun loadActiveStepData(step: LearningStepEntity) {
        viewModelScope.launch {
            try {
                val activePatterns = learningStepRepository.getPatternConfigurations(step.id)
                    .filter {
                        it.isEnabled && if(step.isTest) it.isTest else it.isLearning
                    }
                    .sortedBy { it.order }

                if (activePatterns.isEmpty()) return@launch

                var patternResults = mutableListOf<PatternResult>()
                val testMode = step.isTest
                val repetitions = if (testMode) step.testRepetitions else step.learningRepetitions
                val learningStepResult = LearningStepResult(
                    name = step.name,
                    isExample = step.isExample,
                    testMode = testMode,
                    showStartingPoint = if (testMode) false else step.learningStartingPointEnabled,
                    timeLimit = if (testMode) step.testTimeLimit else step.learningTimeLimit,
                    attempts = if (testMode) 1 else step.learningAttempts,
                    randomPatternOrder = if (testMode) false else step.learningRandomPatternOrder
                )
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
                                patternVariety = p.patternVariety
                            )

                            patternResults.add(
                                PatternResult(
                                    patternId = p.patternId,
                                    patternName = patternEntity.name,
                                    originalName = patternEntity.name,
                                    config = config,
                                    attempts = 1,
                                    exampleKey = patternEntity.exampleKey
                                )
                            )
                        }
                    }
                }

                val preparedResults = preparePatterns(patternResults, step.learningRandomPatternOrder, testMode)

                _state.update { it.copy(
                    patternResults = preparedResults,
                    learningStepResult = learningStepResult,
                ) }
            } catch (_: Exception) {}
        }
    }

    private fun preparePatterns(
        patterns: MutableList<PatternResult>,
        shouldShuffle: Boolean,
        isTestMode: Boolean
    ): List<PatternResult> {
        val prepared = if (shouldShuffle && !isTestMode) {
            patterns.shuffled()
        } else {
            patterns
        }

        // assigning numbers
        val countsByPatternId = mutableMapOf<Long, Int>()

        for (p in prepared) {
            val number = (countsByPatternId[p.patternId] ?: 0) + 1
            countsByPatternId[p.patternId] = number

            p.displayNumber = number
            p.patternName = "${p.originalName} $number"
            p.totalTime = 0.0
        }

        return prepared
    }
}


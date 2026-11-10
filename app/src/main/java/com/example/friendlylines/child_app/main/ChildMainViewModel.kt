package com.example.friendlylines.child_app.main

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.child_app.assessment.AreaComparison
import com.example.friendlylines.child_app.assessment.ShapeCorrectness
import com.example.friendlylines.child_app.reinforcements.ReinforcementEvent
import com.example.friendlylines.child_app.ui.LearningStepResult
import com.example.friendlylines.child_app.ui.PatternConfig
import com.example.friendlylines.child_app.ui.PatternResult
import com.example.shared.data.drafts.PatternConfigOptions
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.drafts.toDp
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.models.toPatternDrawing
import com.example.shared.data.repositories.LearningStepsRepository
import com.example.shared.data.repositories.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.ui.unit.IntSize
import com.example.friendlylines.child_app.assessment.toCanvasPixelStrokes
import com.example.friendlylines.therapist_app.ui.materials.geometry.toNormalized
import com.example.friendlylines.child_app.assessment.AssessmentCriteria
import com.example.friendlylines.child_app.ui.AttemptResult
import com.example.shared.data.drafts.AccuracyLevel

@HiltViewModel
class ChildViewModel  @Inject constructor(
    private val learningStepRepository: LearningStepsRepository,
    private val patternRepository: PatternRepository
)  : ViewModel() {

    /**
     * PARAMETERS, STATES
     */
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _effects = Channel<ReinforcementEvent>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val shapeCorrectness = ShapeCorrectness()

    private var gameTimerJob: Job? = null
    private var finishTimerJob: Job? = null

    private var timeLimitRemainingMillis: Long? = null
    private var timerStartedAt: Long = 0L
    private var isFinishTimerActive = false
    private var canvasSize: IntSize = IntSize.Zero
    private var currentAccuracyLevel = AccuracyLevel.MEDIUM

    private val waitTime: Double = 0.7

    private var density: Float = 1f

    private var currentAttemptNumber = 1

    private data class AttemptScores(val shapeMatch: Int, val insideArea: Int, val outsideArea: Int)

    /**
     * INITIALIZATION
     */
    init {
    viewModelScope.launch {
        learningStepRepository.observeActiveStep().collect { step ->
            if (step != null) {
                loadActiveStepData(step)
            }
        }
    }
}

    /**
     * EVENT HANDLING
     */
    fun setDensity(value: Float) { density = value }

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

    /**
     * DRAWING INPUT HANDLING
     */

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
        startFinishTimer()  // timer after trace is finished, in case user continues drawing
    }

    /**
     * DRAWING ASSESSMENT
     */
    private fun evaluateCurrentDrawing(): AttemptScores {
        val currentState = _state.value
        val referenceDrawing = currentState.currentPatternDrawing

        // shape assessment
        val patternResult = currentState.patternResults.getOrNull(currentState.currentPattern)
        val shapeResult = if (referenceDrawing != null && patternResult != null) {
            val userPoints = currentState.userStrokes.map { stroke ->
                stroke.map { point ->
                    Offset(point.x, point.y).toNormalized(canvasSize)
                }
            }

            val referencePoints = referenceDrawing.toCanvasPixelStrokes(
                canvasSize = canvasSize,
                density = density,
                scaleMultiplier = currentState.patternScale,
                rotationDegrees = currentState.patternRotation,
                smoothing = patternResult.config.smoothingEnabled
            ).map { stroke ->
                stroke.map { point ->
                    Offset(point.x, point.y).toNormalized(canvasSize)
                }
            }

            shapeCorrectness.evaluateShape(
                userStrokes = userPoints,
                referenceStrokes = referencePoints
            )
        } else {
            null
        }

        // area assessment
        val areaResult = if (referenceDrawing != null && patternResult != null) {
            val thicknessPx = patternResult.config.patternThicknessDp.value * density
            AreaComparison.evaluatePatternDrawing(
                userStrokes = currentState.userStrokes,
                patternDrawing = referenceDrawing,
                canvasSize = canvasSize,
                accuracyLevel = currentAccuracyLevel,
                patternThicknessPx = thicknessPx,
                userThicknessPx = thicknessPx,
                density = density,
                scaleMultiplier = currentState.patternScale,
                rotationDegrees = currentState.patternRotation,
                smoothing = patternResult.config.smoothingEnabled
            )
        } else null

        _state.update {
            it.copy(shapeAssessmentResult = shapeResult)
        }

        // update pattern results
        updateLearningStepResults()
        return AttemptScores(
            shapeMatch = shapeResult?.let { (it.score * 100).toInt() } ?: 0,
            insideArea = areaResult?.patternCoveragePercent?.toInt() ?: 0,
            outsideArea = areaResult?.drawingOutsidePercent?.toInt() ?: 0
        )
    }

    private fun updateLearningStepResults() {
        val currentState = _state.value
        val results = currentState.patternResults
        val stepResult = currentState.learningStepResult ?: return

        if (results.isEmpty()) return

        val updatedStepResult = stepResult.copy(
            correctCoverage = results.map {it.insideArea}.average().toInt(),
            lineOutOfBounds = results.map {it.outsideArea}.average().toInt(),
            shapeMatch = results.map {it.shapeMatch}.average().toInt(),
        )

        _state.update { it.copy(learningStepResult = updatedStepResult) }
    }

    /**
     * TIMER HANDLING
     */
    private fun startFinishTimer() {
        finishTimerJob?.cancel()
        finishTimerJob = viewModelScope.launch {
            delay((waitTime * 1000).toLong())
            onDrawingFinished()
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
            onTimeLimitReached()
        }
    }

    /**
     * ATTEMPT HANDLING
     */
    // Time is counted for whole pattern and last (or successful) attempt
    private fun finishAttempt(): Double {
        val limitMillis = (_state.value.learningStepResult?.timeLimit ?: return 0.0) * 1000L
        val remaining = (timeLimitRemainingMillis ?: return 0.0) -
                (System.currentTimeMillis() - timerStartedAt)
        val s = ((limitMillis - remaining).coerceIn(0L, limitMillis) / 1000.0 - waitTime)
            .coerceAtLeast(0.0)

        val result = _state.value.patternResults
            .getOrNull(_state.value.currentPattern) ?: return 0.0
        result.totalTime += s
        result.lastAttemptTime = s
        return s
    }

    private fun restartAttempt() {
        currentAttemptNumber++
        _state.update { it.copy(userStrokes = emptyList(), shapeAssessmentResult = null) }
        startTimeLimitTimer(_state.value.learningStepResult?.timeLimit ?: 0)
    }

    private fun completeAttempt(): Boolean {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()

        val state = _state.value
        val patternResult = state.patternResults.getOrNull(state.currentPattern) ?: return false

        val scores = if (state.userStrokes.isNotEmpty()) evaluateCurrentDrawing()
        else AttemptScores(0, 0, 0)
        val seconds = finishAttempt()
        val success = AssessmentCriteria.isSuccessful(
            currentAccuracyLevel,
            scores.shapeMatch,
            scores.insideArea,
            scores.outsideArea)

        patternResult.attemptResults += AttemptResult(
                    attemptNumber = currentAttemptNumber,
                    shapeMatch = scores.shapeMatch,
                    insideArea = scores.insideArea,
                    outsideArea = scores.outsideArea,
                    timeSeconds = seconds,
                    isSuccessful = success
                )
        patternResult.attempts = patternResult.attemptResults.size
        patternResult.shapeMatch = scores.shapeMatch
        patternResult.insideArea = scores.insideArea
        patternResult.outsideArea = scores.outsideArea

        updateLearningStepResults()
        return success
    }

    private fun hasAttemptsLeft(): Boolean =
        currentAttemptNumber < (_state.value.learningStepResult?.attempts ?: 1)

    // when child stopped drawing
    private fun onDrawingFinished() {
        val success = completeAttempt()
        if (success || !hasAttemptsLeft()) continueAfterDrawing(success) else restartAttempt()
    }

    // time limit ran out
    private fun onTimeLimitReached() {
        val success = completeAttempt()
        if (success || !hasAttemptsLeft()) goToNextPattern() else restartAttempt()
    }

    /**
     * MENU AND NAVIGATION
     */
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
                    currentPatternDrawing = null,
                    shapeAssessmentResult = null
                )
                loadPatternDrawing(nextIndex, newState)
                newState
            } else {
                val results = currentState.patternResults
                if (results.isNotEmpty()) {
                    val avgShape = results.map { it.shapeMatch }.average().toInt()

                    currentState.learningStepResult?.apply {
                        shapeMatch = avgShape
                    }
                }
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
                        state.learningStepResult?.randomPatternOrder == true,
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

    /**
     * PATTERN LOADING AND PREPARATION
     */
    private fun loadPatternDrawing(index: Int, state: GameState) {
        val patternResult = state.patternResults.getOrNull(index) ?: return

        // Start time limit for the new pattern
        startTimeLimitTimer(state.learningStepResult?.timeLimit ?: 0)

        viewModelScope.launch {
            try {
                val patternWithStrokes = patternRepository.getPattern(patternResult.patternId)
                val drawing = patternWithStrokes?.toPatternDrawing()
                val variety = patternResult.config.patternVariety
                currentAttemptNumber = 1
                _state.update {
                    it.copy(
                        currentPatternDrawing = drawing,
                        patternScale = if (variety) kotlin.random.Random.nextFloat() * 0.4f + 0.8f else 1f,
                        patternRotation = if (variety) kotlin.random.Random.nextFloat() * 40f - 20f else 0f
                    )
                }
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

                currentAccuracyLevel = if (step.isTest) step.test.accuracyLevel else step.learning.accuracyLevel
                var patternResults = mutableListOf<PatternResult>()
                val testMode = step.isTest
                val repetitions = if (testMode) step.test.repetitions else step.learning.repetitions
                val learningStepResult = LearningStepResult(
                    name = step.name,
                    isExample = step.isExample,
                    testMode = testMode,
                    showStartingPoint = if (testMode) false else step.learning.startingPointEnabled,
                    timeLimit = if (testMode) step.test.timeLimit else step.learning.timeLimit,
                    attempts = if (testMode) 1 else step.learning.attempts,
                    randomPatternOrder = if (testMode) false else step.learning.randomPatternOrder,
                    praiseWordsEnabled = step.reinforcements.praiseWordsEnabled,
                    praiseWords = step.reinforcements.enabledPraiseWords,
                    animationsEnabled = step.reinforcements.animationsEnabled,
                    animations = step.reinforcements.enabledAnimationThemes,
                    endFanfare = step.reinforcements.endSessionFanfareEnabled,
                    endAnimation = step.reinforcements.endSessionAnimationEnabled
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
                                patternColor = PatternConfigOptions.getPatternAndWritingColor(p.patternColor),
                                traceColor = PatternConfigOptions.getPatternAndWritingColor(p.writingColor),
                                backgroundColor = PatternConfigOptions.getBackgroundColor(p.backgroundColor),
                                patternThickness = p.width,
                                patternThicknessDp = widthEnum.toDp(),
                                smoothingEnabled = patternEntity.smoothingEnabled,
                                patternVariety = p.patternVariety,
                                order = p.order
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

                val preparedResults = preparePatterns(patternResults, step.learning.randomPatternOrder, testMode)

                Log.e("Patterns order", "$preparedResults")

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
            patterns.sortedBy { it.config.order }
        }

        // assigning numbers
        val countsByPatternId = mutableMapOf<Long, Int>()

        for (p in prepared) {
            val number = (countsByPatternId[p.patternId] ?: 0) + 1
            countsByPatternId[p.patternId] = number

            p.displayNumber = number
            p.patternName = "${p.originalName} $number"
            p.totalTime = 0.0
            p.lastAttemptTime = 0.0
            p.attemptResults = emptyList()
            p.attempts = 1
            p.shapeMatch = 0
            p.insideArea = 0
            p.outsideArea = 0
        }

        return prepared
    }

    /**
     * REINFORCEMENT HANDLING
     */
    private fun playPraise(): Boolean {
        val state = _state.value

        if (state.learningStepResult?.testMode != false) return false

        val praisesEnabled = state.learningStepResult.praiseWordsEnabled
        val enabledPraiseWords = state.learningStepResult.praiseWords

        if (enabledPraiseWords.isEmpty() || !praisesEnabled) return false

        val praiseWord = enabledPraiseWords.random()

        val result = _effects.trySend(ReinforcementEvent.PlayPraise(praiseWord))
        return result.isSuccess
    }

    fun onPraisePlaybackFinished() {
        if (!playAnimation()) goToNextPattern()
    }

    private fun playAnimation(): Boolean {
        val state = _state.value

        if (state.learningStepResult?.testMode != false) return false

        val animationsEnabled = state.learningStepResult.animationsEnabled
        val animationsThemes = state.learningStepResult.animations

        if (animationsThemes.isEmpty() || !animationsEnabled) return false

        val theme = animationsThemes.random()
        val result = _effects.trySend(ReinforcementEvent.PlayAnimation(theme))
        return result.isSuccess
    }

    fun onAnimationFinished() {
        goToNextPattern()
    }

    private fun continueAfterDrawing(success: Boolean) {
        if (success && playPraise()) return
        if (success && playAnimation()) return
        goToNextPattern()
    }
}
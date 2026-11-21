package com.example.friendlylines.child_app.main

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.util.Log.e
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
import com.example.friendlylines.child_app.reports.CsvExporter
import com.example.friendlylines.child_app.reports.ReportBuilder
import com.example.friendlylines.child_app.ui.AttemptResult
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.models.Point
import com.example.shared.data.models.PointWithTime
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import com.example.friendlylines.child_app.assessment.StartingPointCheck

@HiltViewModel
class ChildViewModel  @Inject constructor(
    private val learningStepRepository: LearningStepsRepository,
    private val patternRepository: PatternRepository,
    @ApplicationContext private val context: Context
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
    private val recordedStrokes = mutableListOf<MutableList<PointWithTime>>()
    private var attemptStartedAt = 0L
    private var attemptTimeAccumulatedMs = 0L
    private var attemptTimeResumedAt = 0L
    private var attemptTimeRunning = false
    private var canvasSize: IntSize = IntSize.Zero
    private var currentAccuracyLevel = AccuracyLevel.MEDIUM

    private val waitTime: Double = 0.7

    private var density: Float = 1f

    private var currentAttemptNumber = 1

    private data class AttemptScores(
        val shapeMatch: Int,
        val insideArea: Int,
        val outsideArea: Int,
        val shapeDistance: Float?,
        val shapeIsValid: Boolean?,
        val correctStartingPoint: Boolean = false
    )

    private val _recordingEnabled = MutableStateFlow(false)
    val recordingEnabled: StateFlow<Boolean> = _recordingEnabled.asStateFlow()

    fun setRecordingEnabled(enabled: Boolean) {
        _recordingEnabled.value = enabled
    }

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
            is ChildMainEvent.RestartPattern -> handleRestartPattern()
            is ChildMainEvent.BackToMainMenu -> handleBackToMainMenu()
            else -> _state.update { currentState ->
                reduce(currentState, event)
            }
        }
    }

    /**
     * DRAWING INPUT HANDLING
     */
    private fun handlePointerDown(position: Offset) {
        if (_state.value.isReinforcementPlaying) return
        stopTimeLimitCountdown()
        finishTimerJob?.cancel()

        val timeReached = (SystemClock.elapsedRealtime() - attemptStartedAt)/1000f
        recordedStrokes.add(
            mutableListOf(
                PointWithTime(
                    point = Point(x=position.x, y=position.y),
                    timeReached = timeReached
                )
            )
        )
        _state.update { currentState ->
            val newStrokes = currentState.userStrokes + listOf(listOf(position))
            currentState.copy(userStrokes = newStrokes)
        }
    }

    private fun handlePointerMove(position: Offset) {
        if (_state.value.isReinforcementPlaying) return

        val timeReached = (SystemClock.elapsedRealtime() - attemptStartedAt)/1000f
        recordedStrokes.lastOrNull()?.add(
            PointWithTime(
                point = Point(x=position.x, y=position.y),
                timeReached = timeReached
            )
        )
        _state.update { currentState ->
            if (currentState.userStrokes.isEmpty()) return@update currentState
            
            val lastStroke = currentState.userStrokes.last()
            val updatedLastStroke = lastStroke + position
            val newStrokes = currentState.userStrokes.dropLast(1) + listOf(updatedLastStroke)
            
            currentState.copy(userStrokes = newStrokes)
        }
    }

    private fun handlePointerUp() {
        if (_state.value.isReinforcementPlaying) return

        val state = _state.value
        val patternStrokes = state.currentPatternDrawing?.strokes?.size ?: 1
        val timeLimit = state.learningStepResult?.timeLimit ?: 0

        if (state.userStrokes.size < patternStrokes && timeLimit > 0) {
            startStrokeCountdown(timeLimit)   // waiting for the next pattern stroke
        } else {
            startFinishTimer()                // if all strokes drawn
        }
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

        // starting point
        val correctStartingPoint = if (referenceDrawing != null && patternResult != null) {
            val patternPx = referenceDrawing.toCanvasPixelStrokes(
                canvasSize = canvasSize,
                density = density,
                scaleMultiplier = currentState.patternScale,
                rotationDegrees = currentState.patternRotation,
                smoothing = patternResult.config.smoothingEnabled
            )
            StartingPointCheck.isCorrect(
                userStrokes = currentState.userStrokes,
                patternStrokes = patternPx,
                tolerancePx = 48f * density   // margin
            )
        } else false

        _state.update {
            it.copy(shapeAssessmentResult = shapeResult)
        }

        // update pattern results
        updateLearningStepResults()
        return AttemptScores(
            shapeMatch = shapeResult?.let { (it.score * 100).toInt() } ?: 0,
            insideArea = areaResult?.patternCoveragePercent?.toInt() ?: 0,
            outsideArea = areaResult?.drawingOutsidePercent?.toInt() ?: 0,
            shapeDistance = shapeResult?.distance,
            shapeIsValid = shapeResult?.isValid,
            correctStartingPoint = correctStartingPoint
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

    private fun saveStrokesTrajectory(
    ): List<List<PointWithTime>> {
        if (canvasSize == IntSize.Zero) return emptyList()
        val timeInterval = 0.1f

        return recordedStrokes.mapNotNull { stroke ->
            if (stroke.isEmpty()) return@mapNotNull null

            val sampledPoints = mutableListOf<PointWithTime>()
            sampledPoints.add(stroke.first())
            var lastSavedTime = stroke.first().timeReached

            for (point in stroke.drop(1)) {
                if (point.timeReached - lastSavedTime >= timeInterval) {
                    sampledPoints.add(point)
                    lastSavedTime = point.timeReached
                }
            }

            if (sampledPoints.last() != stroke.last()) {
                sampledPoints.add(stroke.last())
            }

            sampledPoints.map { timedPoint ->
                val normalized = Offset(
                    timedPoint.point.x,
                    timedPoint.point.y
                ).toNormalized(canvasSize)

                PointWithTime(
                    point = Point(
                        x = normalized.x,
                        y = normalized.y
                    ),
                    timeReached = timedPoint.timeReached
                )
            }
        }
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

    // new attempt: reset time measurement and recorded strokes
    private fun startTimeLimitTimer(limitSeconds: Int) {
        startAttemptClock()
        attemptStartedAt = SystemClock.elapsedRealtime()
        recordedStrokes.clear()
        startStrokeCountdown(limitSeconds)
    }

    private fun startStrokeCountdown(limitSeconds: Int) {
        if (limitSeconds <= 0) {
            timeLimitRemainingMillis = null
            return
        }
        gameTimerJob?.cancel()
        timeLimitRemainingMillis = limitSeconds * 1000L
        startTimerWithMillis(timeLimitRemainingMillis!!)
    }

    // stop the countdown
    private fun stopTimeLimitCountdown() {
        gameTimerJob?.cancel()
        timeLimitRemainingMillis = null
    }

    private fun startAttemptClock() {
        attemptTimeAccumulatedMs = 0L
        attemptTimeResumedAt = SystemClock.elapsedRealtime()
        attemptTimeRunning = true
    }

    private fun pauseAttemptClock() {
        if (!attemptTimeRunning) return
        attemptTimeAccumulatedMs += SystemClock.elapsedRealtime() - attemptTimeResumedAt
        attemptTimeRunning = false
    }

    private fun resumeAttemptClock() {
        if (attemptTimeRunning) return
        attemptTimeResumedAt = SystemClock.elapsedRealtime()
        attemptTimeRunning = true
    }

    private fun attemptElapsedSeconds(): Double {
        val running = if (attemptTimeRunning) SystemClock.elapsedRealtime() - attemptTimeResumedAt else 0L
        return (attemptTimeAccumulatedMs + running) / 1000.0
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
    private fun finishAttempt(finishDelaySeconds: Double): Double {
        val s = (attemptElapsedSeconds() - finishDelaySeconds).coerceAtLeast(0.0)

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

    // saves attempt data
    private fun completeAttempt(finishDelaySeconds: Double = waitTime): Boolean {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()

        val state = _state.value
        val patternResult = state.patternResults.getOrNull(state.currentPattern) ?: return false

        val scores = if (state.userStrokes.isNotEmpty()) evaluateCurrentDrawing()
        else AttemptScores(0, 0, 0, shapeDistance = null, shapeIsValid = null)
        val trajectory = saveStrokesTrajectory()

        val seconds = finishAttempt(finishDelaySeconds)
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
            isSuccessful = success,
            trajectory = trajectory,
            shapeDistance = scores.shapeDistance,
            shapeIsValid = scores.shapeIsValid,
            correctStartingPoint = scores.correctStartingPoint
        )

        patternResult.attempts = patternResult.attemptResults.size
        patternResult.shapeMatch = scores.shapeMatch
        patternResult.insideArea = scores.insideArea
        patternResult.outsideArea = scores.outsideArea
        patternResult.correctStartingPoint = scores.correctStartingPoint

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
        val success = completeAttempt(finishDelaySeconds = 0.0)
        if (success || !hasAttemptsLeft()) goToNextPattern() else restartAttempt()
    }

    /**
     * MENU AND NAVIGATION
     */
    private fun handleToggleMenu() {
        val currentlyOpen = _state.value.isMenuOpen
        if (!currentlyOpen) {
            // Opening menu - Pause
            pauseAttemptClock()
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
            resumeAttemptClock()
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

    // Restart button: removes the strokes and starts the attempt again
    private fun handleRestartPattern() {
        if (_state.value.isReinforcementPlaying) return

        gameTimerJob?.cancel()
        finishTimerJob?.cancel()
        isFinishTimerActive = false

        _state.update {
            it.copy(
                userStrokes = emptyList(),
                shapeAssessmentResult = null,
                isMenuOpen = false
            )
        }
        startTimeLimitTimer(_state.value.learningStepResult?.timeLimit ?: 0)
    }

    private fun handleBackToMainMenu() {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()
        timeLimitRemainingMillis = null
        isFinishTimerActive = false
        _state.update { it.copy(
            screenState = "main",
            userStrokes = emptyList(),
            isMenuOpen = false,
            isReinforcementPlaying = false
        ) }
    }

    private fun goToNextPattern() {
        gameTimerJob?.cancel()
        finishTimerJob?.cancel()
        timeLimitRemainingMillis = null
        isFinishTimerActive = false

        val currentState = _state.value
        val nextIndex = currentState.currentPattern + 1
        if (nextIndex < currentState.patternResults.size) {
            _state.update { state ->
                val newState = currentState.copy(
                    currentPattern = nextIndex,
                    userStrokes = emptyList(),
                    currentPatternDrawing = null,
                    shapeAssessmentResult = null,
                    isReinforcementPlaying = false
                )
                loadPatternDrawing(nextIndex, newState)
                newState
            }
        } else {
            val results = currentState.patternResults
            if (results.isNotEmpty()) {
                val avgShape = results.map { it.shapeMatch }.average().toInt()

                currentState.learningStepResult?.apply {
                    shapeMatch = avgShape
                }
            }
            if (_recordingEnabled.value) {
                val csvContent = ReportBuilder.buildTrajectoryCsv(results)

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        CsvExporter.save(context, csvContent)
                        Log.i("CSV_EXPORT", "File saved successfully")
                    } catch (exception: Exception) {
                        Log.e("CSV_EXPORT", "Could not save file", exception)
                    }
                }
            }
             _recordingEnabled.value = false
            _state.update { it.copy(screenState = "end", isReinforcementPlaying = false) }
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
    private fun applyColorVariety(patternResult: PatternResult) {
        if (!patternResult.config.colorVariety) return
        val colors = PatternConfigOptions.randomColorSet()
        patternResult.config = patternResult.config.copy(
            patternColor = colors.pattern,
            traceColor = colors.trace,
            backgroundColor = colors.background
        )
    }

    private fun loadPatternDrawing(index: Int, state: GameState) {
        val patternResult = state.patternResults.getOrNull(index) ?: return
        applyColorVariety(patternResult)
        currentAttemptNumber = 1

        // Start time limit for the new pattern
        startTimeLimitTimer(state.learningStepResult?.timeLimit ?: 0)

        viewModelScope.launch {
            try {
                val patternWithStrokes = patternRepository.getPattern(patternResult.patternId)
                val drawing = patternWithStrokes?.toPatternDrawing()
                val variety = patternResult.config.patternVariety
                _state.update {
                    it.copy(
                        currentPatternDrawing = drawing,
                        patternScale = if (variety) kotlin.random.Random.nextFloat() * 0.4f + 0.8f else 1f,
                        patternRotation = if (variety) kotlin.random.Random.nextFloat() * 40f - 20f else 0f
                    )
                }
            } catch (e : Exception) {
                Log.e("ChildViewModel", "Could not load pattern", e)
            }
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
                val patternResults = mutableListOf<PatternResult>()
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
                                order = p.order,
                                colorVariety = p.colorVariety
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
            } catch (e : Exception) {
                Log.e("ChildViewModel", "Could not load pattern", e)
            }
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
        if (result.isSuccess) _state.update { it.copy(isReinforcementPlaying = true) }
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
        if (result.isSuccess) _state.update { it.copy(isReinforcementPlaying = true) }
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
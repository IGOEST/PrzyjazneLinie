package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.ui.configuration.config.PatternConfigColors
import com.example.friendlylines.therapist_app.ui.configuration.patterns.MoveDirection
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.entities.LearningStepPatternEntity
import com.example.shared.data.models.PatternItem
import com.example.shared.data.models.toPatternDrawing
import com.example.shared.data.repositories.LearningStepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel shared by all tabs of the learning step configuration
// Keeps the complete learning step configuration before it is saved
@HiltViewModel
class LearningStepsSettingsViewModel @Inject constructor(
    private val learningStepRepository: LearningStepRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    // patterns
    private val _patterns =
        MutableStateFlow<List<LearningStepsPatternConfigDraft>>(emptyList())

    val patterns: StateFlow<List<LearningStepsPatternConfigDraft>> =
        _patterns.asStateFlow()

    // learning settings
    private val _learningRepetitions = MutableStateFlow(3)
    val learningRepetitions: StateFlow<Int> =
        _learningRepetitions.asStateFlow()

    private val _learningAttempts = MutableStateFlow(3)
    val learningAttempts: StateFlow<Int> =
        _learningAttempts.asStateFlow()

    private val _learningTimeLimit = MutableStateFlow(30)
    val learningTimeLimit: StateFlow<Int> =
        _learningTimeLimit.asStateFlow()

    private val _learningAccuracy = MutableStateFlow(AccuracyLevel.MEDIUM)
    val learningAccuracy: StateFlow<AccuracyLevel> =
        _learningAccuracy.asStateFlow()

    private val _startingPointEnabled = MutableStateFlow(false)
    val startingPointEnabled: StateFlow<Boolean> =
        _startingPointEnabled.asStateFlow()

    private val _randomPatternOrder = MutableStateFlow(false)
    val randomPatternOrder: StateFlow<Boolean> =
        _randomPatternOrder.asStateFlow()

    // test settings
    private val _testRepetitions = MutableStateFlow(3)
    val testRepetitions: StateFlow<Int> =
        _testRepetitions.asStateFlow()

    private val _testTimeLimit = MutableStateFlow(30)
    val testTimeLimit: StateFlow<Int> =
        _testTimeLimit.asStateFlow()

    private val _testAccuracy = MutableStateFlow(AccuracyLevel.MEDIUM)
    val testAccuracy: StateFlow<AccuracyLevel> =
        _testAccuracy.asStateFlow()

    // database id of the currently edited learning step
    private val _learningStepId = MutableStateFlow<Long?>(null)
    val learningStepId: StateFlow<Long?> =
        _learningStepId.asStateFlow()

    fun setName(value: String) {
        _name.value = value
    }

    // patterns
    fun addPattern(
        pattern: PatternItem,
        width: PatternWidth,
        patternColor: ColorOption?,
        writingColor: ColorOption?,
        backgroundColor: ColorOption?,
        patternVariety: Boolean
    ): Long {

        val newId = System.currentTimeMillis()

        _patterns.update { patterns ->
            patterns + LearningStepsPatternConfigDraft(
                id = newId,
                pattern = pattern,
                width = width,
                patternColor = patternColor,
                writingColor = writingColor,
                backgroundColor = backgroundColor,
                patternVariety = patternVariety,
                order = patterns.size
            )
        }

        return newId
    }

    fun updatePattern(
        configId: Long,
        patternItem: PatternItem,
        width: PatternWidth,
        patternColor: ColorOption?,
        writingColor: ColorOption?,
        backgroundColor: ColorOption?,
        patternVariety: Boolean
    ) {
        _patterns.update { patterns ->
            patterns.map { config ->
                if (config.id == configId) {
                    config.copy(
                        pattern = patternItem,
                        width = width,
                        patternColor = patternColor,
                        writingColor = writingColor,
                        backgroundColor = backgroundColor,
                        patternVariety = patternVariety
                    )
                } else {
                    config
                }
            }
        }
    }

    fun deletePattern(configId: Long) {
        _patterns.update { patterns ->
            patterns
                .filterNot { it.id == configId }
                .mapIndexed { index, pattern ->
                    pattern.copy(order = index)
                }
        }
    }

    fun setPatternEnabled(
        configId: Long,
        enabled: Boolean
    ) {
        _patterns.update { patterns ->
            patterns.map { pattern ->
                if (pattern.id == configId) {
                    pattern.copy(isEnabled = enabled)
                } else {
                    pattern
                }
            }
        }
    }

    fun copyPattern(configId: Long): Long? {
        val source = _patterns.value.find {
            it.id == configId
        } ?: return null

        val newId = System.currentTimeMillis()

        _patterns.update { patterns ->
            patterns + source.copy(
                id = newId,
                order = patterns.size
            )
        }

        return newId
    }

    fun movePattern(
        configId: Long,
        direction: MoveDirection
    ) {
        _patterns.update { patterns ->

            val currentIndex = patterns.indexOfFirst {
                it.id == configId
            }

            if (currentIndex == -1) {
                return@update patterns
            }

            val newIndex = when (direction) {
                MoveDirection.UP -> currentIndex - 1
                MoveDirection.DOWN -> currentIndex + 1
            }

            if (newIndex !in patterns.indices) {
                return@update patterns
            }

            val updated = patterns.toMutableList()

            val temp = updated[currentIndex]
            updated[currentIndex] = updated[newIndex]
            updated[newIndex] = temp

            updated.mapIndexed { index, pattern ->
                pattern.copy(order = index)
            }
        }
    }
    // learning setters
    fun setLearningRepetitions(value: Int) {
        _learningRepetitions.value = value
    }

    fun setLearningAttempts(value: Int) {
        _learningAttempts.value = value
    }

    fun setLearningTimeLimit(value: Int) {
        _learningTimeLimit.value = value
    }

    fun setLearningAccuracy(value: AccuracyLevel) {
        _learningAccuracy.value = value
    }

    fun setStartingPointEnabled(value: Boolean) {
        _startingPointEnabled.value = value
    }

    fun setRandomPatternOrder(value: Boolean) {
        _randomPatternOrder.value = value
    }

    // test setters
    fun setTestRepetitions(value: Int) {
        _testRepetitions.value = value
    }

    fun setTestTimeLimit(value: Int) {
        _testTimeLimit.value = value
    }

    fun setTestAccuracy(value: AccuracyLevel) {
        _testAccuracy.value = value
    }

    // saves pattern configuration
    private suspend fun savePatternConfigurations(
        learningStepId: Long
    ) {
        // replace pattern configurations with current state - delete old
        learningStepRepository.deletePatternConfigurations(
            learningStepId
        )

        // // convert pattern drafts to database entities
        val patternEntities = _patterns.value.map { patternConfig ->
            LearningStepPatternEntity(
                learningStepId = learningStepId,
                patternId = patternConfig.pattern.pattern.id,
                width = patternConfig.width.name,
                patternColor = patternConfig.patternColor?.key,
                writingColor = patternConfig.writingColor?.key,
                backgroundColor = patternConfig.backgroundColor?.key,
                patternVariety = patternConfig.patternVariety,
                order = patternConfig.order,
                isEnabled = patternConfig.isEnabled
            )
        }

        // save all pattern configurations
        if (patternEntities.isNotEmpty()) {
            learningStepRepository.savePatternConfigurations(
                patternEntities
            )
        }
    }

    // saves/updates the complete learning step configuration
    fun saveLearningStep(
        name: String,
        onSaved: (Long) -> Unit
    ) {
        viewModelScope.launch {

            val existingLearningStepId = _learningStepId.value

            val learningStepId = if (existingLearningStepId == null) {

                // save new learning step
                learningStepRepository.saveLearningStep(
                    name = name,
                    repetitions = _learningRepetitions.value,
                    attempts = _learningAttempts.value,
                    timeLimit = _learningTimeLimit.value,
                    accuracyLevel = _learningAccuracy.value.name,
                    startingPointEnabled = _startingPointEnabled.value,
                    randomPatternOrder = _randomPatternOrder.value,
                    testRepetitions = _testRepetitions.value,
                    testTimeLimit = _testTimeLimit.value,
                    testAccuracyLevel = _testAccuracy.value.name
                )

            } else {

                // edit existing learning step
                learningStepRepository.updateLearningStep(
                    learningStepId = existingLearningStepId,
                    name = name,
                    repetitions = _learningRepetitions.value,
                    attempts = _learningAttempts.value,
                    timeLimit = _learningTimeLimit.value,
                    accuracyLevel = _learningAccuracy.value.name,
                    startingPointEnabled = _startingPointEnabled.value,
                    randomPatternOrder = _randomPatternOrder.value,
                    testRepetitions = _testRepetitions.value,
                    testTimeLimit = _testTimeLimit.value,
                    testAccuracyLevel = _testAccuracy.value.name
                )

                existingLearningStepId
            }

            // save pattern configuration
            savePatternConfigurations(learningStepId)

            // remember the database id
            _learningStepId.value = learningStepId

            // tell the UI that saving is complete
            onSaved(learningStepId)
        }
    }

    // save current state of learning step (used for saving patterns while editing step)
    fun saveCurrentLearningStep(
        onSaved: () -> Unit = {}
    ) {
        saveLearningStep(
            name = _name.value
        ) {
            onSaved()
        }
    }

    // loads the saved learning step
    fun loadLearningStep(stepId: Long) {
        viewModelScope.launch {
            val step = learningStepRepository.getLearningStep(stepId)
                ?: return@launch

            _learningStepId.value = step.id
            _name.value = step.name

            _learningRepetitions.value = step.repetitions
            _learningAttempts.value = step.attempts
            _learningTimeLimit.value = step.timeLimit
            _learningAccuracy.value =
                AccuracyLevel.valueOf(step.accuracyLevel)

            _startingPointEnabled.value =
                step.startingPointEnabled

            _randomPatternOrder.value =
                step.randomPatternOrder

            _testRepetitions.value =
                step.testRepetitions

            _testTimeLimit.value =
                step.testTimeLimit

            _testAccuracy.value =
                AccuracyLevel.valueOf(step.testAccuracyLevel)

            val patternDrafts =
                learningStepRepository.getPatternItemsForLearningStep(
                    step.id
                )

            _patterns.value = patternDrafts
        }
    }

    // resetting screen
    fun resetForNewLearningStep() {
        _name.value = ""

        _patterns.value = emptyList()

        _learningRepetitions.value = 3
        _learningAttempts.value = 3
        _learningTimeLimit.value = 30
        _learningAccuracy.value = AccuracyLevel.MEDIUM
        _startingPointEnabled.value = false
        _randomPatternOrder.value = false

        _testRepetitions.value = 3
        _testTimeLimit.value = 30
        _testAccuracy.value = AccuracyLevel.MEDIUM

        _learningStepId.value = null
    }

    // checking if the name of learning step is already taken
    fun isLearningStepNameTaken(
        name: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val exists = learningStepRepository.learningStepExistsByName(
                name = name,
                excludeId = _learningStepId.value
            )

            onResult(exists)
        }
    }
}
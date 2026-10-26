package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.components.NameError
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningEvent
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.reinforcement.LearningStepsReinforcementsScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryEvent
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestEvent
import com.example.shared.data.drafts.LearningStepsDraft
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsTestDraft
import com.example.shared.data.repositories.LearningStepsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningStepsSettingsViewModel @Inject constructor(
    private val learningStepsRepository: LearningStepsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val stepId: Long? = savedStateHandle.get<Long>("stepId") ?.takeIf { it != -1L }
    private val _state = MutableStateFlow(LearningStepsSettingsState())
    val state: StateFlow<LearningStepsSettingsState> = _state.asStateFlow()

    init {
        stepId?.let {
            loadLearningStep(it)
        }
    }

    fun onEvent(event: LearningStepsSettingsEvent) {
        when(event) {
            is LearningStepsSettingsEvent.Patterns -> {
                _state.update {
                    it.copy(
                        patternState = LearningStepsPatternsScreenViewModel.reduce(
                            state = it.patternState,
                            event = event.event
                        )
                    )
                }
            }

            is LearningStepsSettingsEvent.Learning -> {
                onLearningEvent(event)
            }

            is LearningStepsSettingsEvent.Test -> {
                onTestEvent(event)
            }

            is LearningStepsSettingsEvent.Reinforcements -> {
                _state.update {
                    it.copy(
                        reinforcementsState = LearningStepsReinforcementsScreenViewModel.reduce(
                            state = it.reinforcementsState,
                            event = event.event
                        )
                    )
                }
            }

            is LearningStepsSettingsEvent.Summary -> {
                when (event.event) {
                    LearningStepsSummaryEvent.SaveClicked -> {
                        _state.update {
                            it.copy(
                                showSaveDialog = true
                            )
                        }
                    }

                    LearningStepsSummaryEvent.SaveConfirmed -> {
                        val currentState = _state.value

                        val name = currentState.stepName.trim()

                        if (name.isBlank()) {
                            _state.update {
                                it.copy(
                                    showSaveDialog = false,
                                    stepNameError = NameError.BLANK
                                )
                            }
                            return
                        }

                        viewModelScope.launch {
                            try {
                                val nameExists = learningStepsRepository.existsLearningStepByName(
                                    name = name,
                                    excludedId = stepId ?: 0L
                                )

                                if (nameExists) {
                                    _state.update {
                                        it.copy(
                                            showSaveDialog = false,
                                            stepNameError = NameError.EXISTS
                                        )
                                    }
                                    return@launch
                                }

                                val draft = LearningStepsDraft(
                                    id = stepId ?: 0L,
                                    name = name,
                                    patterns = currentState.patternState,
                                    learning = currentState.learningState,
                                    test = currentState.testState,
                                    reinforcements = currentState.reinforcementsState
                                )

                                val savedLearningStepId = if (stepId == null) {
                                    learningStepsRepository.saveLearningStep(draft)
                                } else {
                                    learningStepsRepository.updateLearningStep(draft)
                                    stepId
                                }

                                _state.update {
                                    it.copy(
                                        showSaveDialog = false,
                                        savedLearningStepId = savedLearningStepId
                                    )
                                }
                            } catch (e: Exception) { throw e }
                        }
                    }

                    is LearningStepsSummaryEvent.NameChanged -> {
                        _state.update {
                            it.copy(
                                stepName = event.event.name,
                                stepNameError = null
                            )
                        }
                    }

                    is LearningStepsSummaryEvent.SaveDialogDismissed -> {
                        _state.update {
                            it.copy(
                                showSaveDialog = false
                            )
                        }
                    }
                }
            }

            LearningStepsSettingsEvent.SaveSuccessHandled -> {
                _state.update {
                    it.copy(
                        savedLearningStepId = null
                    )
                }
            }

            is LearningStepsSettingsEvent.NameChanged -> {
                _state.update {
                    it.copy(
                        stepName = event.name
                    )
                }
            }

            else -> {}
        }
    }

    private fun loadLearningStep(stepId: Long) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            try {
                val draft = learningStepsRepository.getLearningStep(stepId) ?: return@launch

                if (draft != null) {
                    _state.update {
                        it.copy(
                            stepName = draft.name,
                            patternState = draft.patterns,
                            learningState = draft.learning,
                            testState = draft.test,
                            reinforcementsState = draft.reinforcements
                        )
                    }
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onTestEvent(event: LearningStepsSettingsEvent.Test) {
        when (event.event) {
            is LearningStepsTestEvent.OverrideLearning ->
                setTestOverridesLearning(event.event.override)

            is LearningStepsTestEvent.SetRepetitions ->
                updateTestParameters {
                    it.copy(repetitions = event.event.value)
                }

            is LearningStepsTestEvent.SetTimeLimit ->
                updateTestParameters {
                    it.copy(timeLimit = event.event.value)
                }

            is LearningStepsTestEvent.SetAccuracyLevel ->
                updateTestParameters {
                    it.copy(accuracyLevel = event.event.value)
                }
        }
    }

    fun deriveTestParameters(
        learning: LearningStepsLearningDraft
    ): LearningStepsTestDraft {
        return LearningStepsTestDraft(
            overridesLearning = false,
            repetitions = learning.repetitions,
            attempts = 1,
            timeLimit = learning.timeLimit,
            accuracyLevel = learning.accuracyLevel,
            startingPointEnabled = false,
            randomPatternOrder = false
        )
    }

    fun onLearningEvent(event: LearningStepsSettingsEvent.Learning) {
        when (val learningEvent = event.event) {
            is LearningStepsLearningEvent.SetRepetitions ->
                updateLearningParameters{
                    it.copy(repetitions = learningEvent.value)
                }

            is LearningStepsLearningEvent.SetAttempts ->
                updateLearningParameters {
                    it.copy(attempts = learningEvent.value)
                }

            is LearningStepsLearningEvent.SetTimeLimit ->
                updateLearningParameters {
                    it.copy(timeLimit = learningEvent.value)
                }

            is LearningStepsLearningEvent.SetAccuracyLevel ->
                updateLearningParameters {
                    it.copy(accuracyLevel = learningEvent.value)
                }

            is LearningStepsLearningEvent.SetRandomPatternOrder ->
                updateLearningParameters {
                    it.copy(randomPatternOrder = learningEvent.enabled)
                }

            is LearningStepsLearningEvent.SetStartingPointEnabled ->
                updateLearningParameters {
                    it.copy(startingPointEnabled = learningEvent.enabled)
                }
        }
    }

    fun updateLearningParameters(
        transform: (LearningStepsLearningDraft) -> LearningStepsLearningDraft
    ) {
        _state.update { current ->
            val nextLearning = transform(current.learningState)

            val nextTest =
                if (current.testState.overridesLearning) {
                    current.testState
                } else {
                    deriveTestParameters(nextLearning)
                }

            current.copy(
                learningState = nextLearning,
                testState = nextTest
            )
        }
    }

    fun updateTestParameters(
        transform: (LearningStepsTestDraft) -> LearningStepsTestDraft
    ) {
        _state.update { current ->
            current.copy(
                testState = transform(current.testState)
            )
        }
    }

    fun setTestOverridesLearning(overridesLearning: Boolean) {
        _state.update { current ->
            val nextTest =
                if (overridesLearning) {
                    current.testState.copy(overridesLearning = true)
                } else {
                    deriveTestParameters(current.learningState)
                }

            current.copy(
                testState = nextTest
            )
        }
    }
}
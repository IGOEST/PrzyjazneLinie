package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryEvent
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestScreenViewModel
import com.example.friendlylines.therapist_app.ui.materials.models.NameError
import com.example.shared.data.drafts.LearningStepsDraft
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
                _state.update {
                    it.copy(
                        learningState = LearningStepsLearningScreenViewModel.reduce(
                            it.learningState,
                            event.event
                        )
                    )
                }
            }

            is LearningStepsSettingsEvent.Test -> {
                _state.update {
                    it.copy(
                        testState = LearningStepsTestScreenViewModel.reduce(
                            it.testState,
                            event.event
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
                                    test = currentState.testState
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
                            } catch (e: Exception) { }
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
            val draft = learningStepsRepository.getLearningStep(stepId) ?: return@launch

            _state.update {
                it.copy(
                    stepName = draft.name,
                    patternState = draft.patterns,
                    learningState = draft.learning,
                    testState = draft.test
                )
            }
        }
    }
}
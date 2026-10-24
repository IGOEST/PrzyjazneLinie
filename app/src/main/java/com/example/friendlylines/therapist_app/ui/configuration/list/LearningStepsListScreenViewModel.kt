package com.example.friendlylines.therapist_app.ui.configuration.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repositories.LearningStepsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LearningStepsListScreenViewModel @Inject constructor(
    private val learningStepsRepository: LearningStepsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LearningStepsListState())

    val state: StateFlow<LearningStepsListState> = _state.asStateFlow()

    private val _canPlay = MutableStateFlow(false)
    val canPlay = _canPlay.asStateFlow()

    init {
        loadLearningSteps()
    }

    fun onEvent(event: LearningStepsListEvent) {
        when (event) {
            is LearningStepsListEvent.ActiveChanged -> {
                viewModelScope.launch {
                    learningStepsRepository.updateLearningStepActive(
                        id = event.id,
                        isActive = event.isActive
                    )

                    refreshList()
                }
            }

            is LearningStepsListEvent.ModeChanged -> {
                viewModelScope.launch {
                    learningStepsRepository.updateLearningStepMode(
                        id = event.id,
                        isTest = event.isTest
                    )

                    refreshList()
                }
            }

            is LearningStepsListEvent.CopyClicked -> {
                viewModelScope.launch {
                    val newLearningStepId = learningStepsRepository.copyLearningStep(
                        id = event.id,
                        copySuffix = event.copySuffix
                    )

                    _state.update {
                        it.copy(
                            copiedLearningStepId = newLearningStepId
                        )
                    }

                    refreshList()
                }
            }

            is LearningStepsListEvent.DeleteClicked -> {
                viewModelScope.launch {
                    learningStepsRepository.deleteLearningStep(event.id)
                    refreshList()
                }
            }

            is LearningStepsListEvent.CopiedLearningStepHandled -> {
                _state.update {
                    it.copy(copiedLearningStepId = null)
                }
            }

            is LearningStepsListEvent.SearchQueryChanged -> {
                _state.update {
                    it.copy(searchQuery = event.query)
                }
            }

            is LearningStepsListEvent.HideExampleStepsChanged -> {
                _state.update {
                    it.copy(
                        hideExampleSteps = event.hide
                    )
                }
            }
        }
    }

    fun loadLearningSteps() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            try {
                refreshList()

                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }

                throw e
            } finally {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private suspend fun refreshList() {
        val learningSteps = learningStepsRepository.getAllLearningSteps()

        val activeStep = learningSteps.firstOrNull { it.isActive }

        val canPlay = activeStep?.let { step ->
            learningStepsRepository
                .getPatternConfigurations(step.id)
                .any { pattern ->
                    pattern.isEnabled &&
                            if (step.isTest) {
                                pattern.isTest
                            } else {
                                pattern.isLearning
                            }
                }
        } ?: false

        _state.update {
            it.copy(
                learningSteps = learningSteps
            )
        }

        _canPlay.value = canPlay
    }
}
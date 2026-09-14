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

                    loadLearningSteps()
                }
            }

            is LearningStepsListEvent.ModeChanged -> {
                viewModelScope.launch {
                    learningStepsRepository.updateLearningStepMode(
                        id = event.id,
                        isTest = event.isTest
                    )

                    loadLearningSteps()
                }
            }

            is LearningStepsListEvent.CopyClicked -> {
                viewModelScope.launch {
                    val newLearningStepId = learningStepsRepository.copyLearningStep(event.id)

                    _state.update {
                        it.copy(
                            copiedLearningStepId = newLearningStepId
                        )
                    }

                    loadLearningSteps()
                }
            }

            is LearningStepsListEvent.DeleteClicked -> {
                viewModelScope.launch {
                    learningStepsRepository.deleteLearningStep(event.id)
                    loadLearningSteps()
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
        }
    }

    fun loadLearningSteps() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val learningSteps = learningStepsRepository.getAllLearningSteps()

            _state.update {
                it.copy(
                    learningSteps = learningSteps,
                    isLoading = false
                )
            }
        }
    }
}
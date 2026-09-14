package com.example.friendlylines.therapist_app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.repositories.LearningStepsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val learningStepsRepository: LearningStepsRepository
) : ViewModel() {

    private val _activeLearningStep = MutableStateFlow<LearningStepEntity?>(null)

    val activeLearningStep: StateFlow<LearningStepEntity?> = _activeLearningStep.asStateFlow()

    fun loadActiveLearningStep() {
        viewModelScope.launch {
            _activeLearningStep.value = learningStepsRepository.getActiveStep()
        }
    }
}
package com.example.friendlylines.therapist_app.ui.configuration.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.entities.LearningStepMode
import com.example.shared.data.repositories.LearningStepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningStepsListScreenViewModel @Inject constructor(
    private val learningStepRepository: LearningStepRepository
) : ViewModel() {

    // contains all learning steps saved in the database
    private val _learningSteps = MutableStateFlow<List<LearningStepEntity>>(emptyList())

    // exposes the saved learning steps to the screen
    val learningSteps: StateFlow<List<LearningStepEntity>> =
        _learningSteps.asStateFlow()

    // loads learning steps from the database
    fun loadLearningSteps() {
        viewModelScope.launch {
            _learningSteps.value = learningStepRepository.getAllLearningSteps()
        }
    }

    // mark step as active
    fun setActiveStep(stepId: Long) {
        viewModelScope.launch {
            learningStepRepository.setActiveStep(stepId)
            loadLearningSteps()
        }
    }

    fun setMode(
        stepId: Long,
        isTest: Boolean
    ) {
        viewModelScope.launch {
            val mode = if (isTest) {
                LearningStepMode.TEST.name
            } else {
                LearningStepMode.LEARNING.name
            }

            learningStepRepository.updateLearningStepMode(
                learningStepId = stepId,
                mode = mode
            )

            loadLearningSteps()
        }
    }
}
package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.lifecycle.ViewModel
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsEvent
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestScreenViewModel
import com.example.shared.data.drafts.LearningStepsTestDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LearningStepsSettingsViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(LearningStepsSettingsState())

    val state: StateFlow<LearningStepsSettingsState> = _state.asStateFlow()

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

            else -> {}
        }
    }
}
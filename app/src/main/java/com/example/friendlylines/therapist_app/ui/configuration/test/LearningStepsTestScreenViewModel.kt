package com.example.friendlylines.therapist_app.ui.configuration.test

import androidx.lifecycle.ViewModel
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningEvent
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsTestDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LearningStepsTestScreenViewModel @Inject constructor() : ViewModel() {
    private val _draft = MutableStateFlow(LearningStepsTestDraft())
    val draft: StateFlow<LearningStepsTestDraft> = _draft.asStateFlow()

    fun onEvent(event: LearningStepsTestEvent) {
        _draft.update {
            reduce(it, event)
        }
    }

    companion object {

        fun reduce(
            state: LearningStepsTestDraft,
            event: LearningStepsTestEvent
        ): LearningStepsTestDraft {
            return when (event) {
                is LearningStepsTestEvent.SetRepetitions ->
                    state.copy(
                        repetitions = event.value
                    )

                is LearningStepsTestEvent.SetTimeLimit ->
                    state.copy(
                        timeLimit = event.value
                    )

                is LearningStepsTestEvent.SetAccuracyLevel ->
                    state.copy(
                        accuracyLevel = event.value
                    )
            }
        }
    }
}
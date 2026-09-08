package com.example.friendlylines.therapist_app.ui.configuration.learning

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsDraft
import com.example.shared.data.drafts.LearningStepsLearningDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LearningStepsLearningScreenViewModel @Inject constructor() : ViewModel() {

    private val _draft = MutableStateFlow(LearningStepsLearningDraft())
    val draft: StateFlow<LearningStepsLearningDraft> = _draft.asStateFlow()

    fun onEvent(event: LearningStepsLearningEvent) {
        _draft.update {
            reduce(it, event)
        }
    }

    companion object {

        fun reduce(
            state: LearningStepsLearningDraft,
            event: LearningStepsLearningEvent
        ): LearningStepsLearningDraft {
            return when (event) {
                is LearningStepsLearningEvent.SetRepetitions ->
                    state.copy(
                        repetitions = event.value
                    )

                is LearningStepsLearningEvent.SetAttempts ->
                    state.copy(
                        attempts = event.value
                    )

                is LearningStepsLearningEvent.SetTimeLimit ->
                    state.copy(
                        timeLimit = event.value
                    )

                is LearningStepsLearningEvent.SetAccuracyLevel ->
                    state.copy(
                        accuracyLevel = event.value
                    )

                is LearningStepsLearningEvent.SetStartingPointEnabled ->
                    state.copy(
                        startingPointEnabled = event.enabled
                    )

                is LearningStepsLearningEvent.SetRandomPatternOrder ->
                    state.copy(
                        randomPatternOrder = event.enabled
                    )
            }
        }
    }
}
package com.example.friendlylines.therapist_app.ui.configuration.reinforcement

import androidx.lifecycle.ViewModel
import com.example.shared.data.drafts.LearningStepsReinforcementsDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LearningStepsReinforcementsScreenViewModel @Inject constructor() : ViewModel() {
    private val _draft = MutableStateFlow(LearningStepsReinforcementsDraft())

    val draft: StateFlow<LearningStepsReinforcementsDraft> = _draft.asStateFlow()

    fun onEvent(event: LearningStepsReinforcementsEvent) {
        _draft.update { state ->
            reduce(state, event)
        }
    }

    companion object {

        fun reduce(
            state: LearningStepsReinforcementsDraft,
            event: LearningStepsReinforcementsEvent
        ): LearningStepsReinforcementsDraft {
            return when(event) {
                is LearningStepsReinforcementsEvent.SetEnabledPraiseWords -> {
                    state.copy(
                        enabledPraiseWords = event.words.intersect(LearningStepsReinforcementsDraft.PRAISE_WORDS),
                        praiseWordsEnabled = event.enabled
                    )
                }

                is LearningStepsReinforcementsEvent.SetEnabledAnimationThemes -> {
                    state.copy(
                        enabledAnimationThemes = event.themes.intersect(LearningStepsReinforcementsDraft.ANIMATION_THEMES),
                        animationsEnabled = event.enabled
                    )
                }

                is LearningStepsReinforcementsEvent.SetEndSessionAnimationEnabled -> {
                    state.copy(
                        endSessionAnimationEnabled = event.enabled
                    )
                }

                is LearningStepsReinforcementsEvent.SetEndSessionFanfareEnabled -> {
                    state.copy(
                        endSessionFanfareEnabled = event.enabled
                    )
                }
            }
        }
    }
}
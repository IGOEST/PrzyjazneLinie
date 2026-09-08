package com.example.friendlylines.therapist_app.ui.configuration.patterns

import androidx.lifecycle.ViewModel
//import com.example.friendlylines.therapist_app.ui.configuration.config.ColorOption
//import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft
//import com.example.friendlylines.therapist_app.ui.configuration.config.PatternWidth
//import com.example.friendlylines.therapist_app.ui.configuration.list.LearningStepsDraft
//import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningEvent
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreenViewModel
import kotlinx.coroutines.flow.SharingStarted
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft
import com.example.shared.data.models.PatternItem

@HiltViewModel
class LearningStepsPatternsScreenViewModel @Inject constructor() : ViewModel() {

    private val _draft = MutableStateFlow(LearningStepsPatternsDraft())

    val draft: StateFlow<LearningStepsPatternsDraft> = _draft.asStateFlow()

    fun onEvent(event: LearningStepsPatternsEvent) {
        _draft.update {
            reduce(it, event)
        }
    }

    companion object {

        fun reduce(
            state: LearningStepsPatternsDraft,
            event: LearningStepsPatternsEvent
        ): LearningStepsPatternsDraft {

            return when (event) {

                is LearningStepsPatternsEvent.AddPattern -> {
                    val newId = System.currentTimeMillis()

                    val newPattern = LearningStepsPatternConfigDraft(
                        id = newId,
                        pattern = event.pattern,
                        width = event.width,
                        patternColor = event.patternColor,
                        writingColor = event.writingColor,
                        backgroundColor = event.backgroundColor,
                        patternVariety = event.patternVariety,
                        order = state.patterns.size
                    )

                    state.copy(
                        patterns = state.patterns + newPattern,
                        scrollToConfigId = newId
                    )
                }

                is LearningStepsPatternsEvent.SetPatternEnabled -> {
                    state.copy(
                        patterns = state.patterns.map { pattern ->
                            if (pattern.id == event.configId) {
                                pattern.copy(
                                    isEnabled = event.enabled
                                )
                            } else {
                                pattern
                            }
                        }
                    )
                }

                is LearningStepsPatternsEvent.MovePattern -> {
                    val currentIndex = state.patterns.indexOfFirst {
                        it.id == event.configId
                    }

                    if (currentIndex == -1) {
                        return state
                    }

                    val newIndex = when (event.direction) {
                        MoveDirection.UP -> currentIndex - 1
                        MoveDirection.DOWN -> currentIndex + 1
                    }

                    if (newIndex !in state.patterns.indices) {
                        return state
                    }

                    val updatedPatterns = state.patterns.toMutableList()

                    val temp = updatedPatterns[currentIndex]
                    updatedPatterns[currentIndex] = updatedPatterns[newIndex]
                    updatedPatterns[newIndex] = temp

                    state.copy(
                        patterns = updatedPatterns.mapIndexed { index, pattern ->
                            pattern.copy(
                                order = index
                            )
                        }
                    )
                }

                is LearningStepsPatternsEvent.DeletePattern -> {
                    state.copy(
                        patterns = state.patterns
                            .filterNot {
                                it.id == event.configId
                            }
                            .mapIndexed { index, pattern ->
                                pattern.copy(
                                    order = index
                                )
                            }
                    )
                }

                is LearningStepsPatternsEvent.CopyPattern -> {
                    val source = state.patterns.find {
                        it.id == event.configId
                    } ?: return state

                    val newId = System.currentTimeMillis()

                    val copiedPattern = source.copy(
                        id = newId,
                        order = state.patterns.size
                    )

                    state.copy(
                        patterns = state.patterns + copiedPattern,
                        scrollToConfigId = newId
                    )
                }

                is LearningStepsPatternsEvent.UpdatePattern -> {
                    state.copy(
                        patterns = state.patterns.map { config ->
                            if (config.id == event.configId) {
                                config.copy(
                                    pattern = event.pattern,
                                    width = event.width,
                                    patternColor = event.patternColor,
                                    writingColor = event.writingColor,
                                    backgroundColor = event.backgroundColor,
                                    patternVariety = event.patternVariety
                                )
                            } else {
                                config
                            }
                        }
                    )
                }

                is LearningStepsPatternsEvent.ScrollToConfigHandled -> {
                    state.copy(
                        scrollToConfigId = null
                    )
                }
            }
        }
    }
}
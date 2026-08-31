package com.example.friendlylines.therapist_app.ui.configuration.patterns

import androidx.lifecycle.ViewModel
import com.example.friendlylines.therapist_app.ui.configuration.config.ColorOption
import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft
import com.example.friendlylines.therapist_app.ui.configuration.config.PatternWidth
import com.example.friendlylines.therapist_app.ui.configuration.list.LearningStepsDraft
import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted

//@HiltViewModel
//class LearningStepsPatternsScreenViewModel @Inject constructor() : ViewModel() {
//    private val _draft = MutableStateFlow(LearningStepsDraft())
//    val draft: StateFlow<LearningStepsDraft> = _draft.asStateFlow()
//
//    fun addPattern(
//        pattern: PatternItem,
//        widthIndex: Int,
//        patternColor: ColorOption?,
//        writingColor: ColorOption?,
//        backgroundColor: ColorOption?,
//        patternVariety: Boolean
//    ): Long {
//        val newId = generateConfigId()
//
//        _draft.update { draft ->
//            val newPattern = LearningStepsPatternConfigDraft(
//                id = newId,
//                pattern = pattern,
//                widthIndex = widthIndex,
//                patternColor = patternColor,
//                writingColor = writingColor,
//                backgroundColor = backgroundColor,
//                patternVariety = patternVariety,
//                order = draft.patterns.size
//            )
//
//            draft.copy(
//                patterns = draft.patterns + newPattern
//            )
//        }
//
//        return newId
//    }
//
//    fun updatePattern(
//        configId: Long,
//        patternItem: PatternItem,
//        widthIndex: Int,
//        patternColor: ColorOption?,
//        writingColor: ColorOption?,
//        backgroundColor: ColorOption?,
//        patternVariety: Boolean
//    ) {
//        _draft.update { draft ->
//            val updatedPatterns = draft.patterns.map { config ->
//                if (config.id == configId) {
//                    config.copy(
//                        pattern = patternItem,
//                        widthIndex = widthIndex,
//                        patternColor = patternColor,
//                        writingColor = writingColor,
//                        backgroundColor = backgroundColor,
//                        patternVariety = patternVariety
//                    )
//                } else {
//                    config
//                }
//            }
//
//            draft.copy(
//                patterns = updatedPatterns
//            )
//        }
//    }
//
//    fun deletePattern(configId: Long) {
//        _draft.update { draft ->
//            val updatedPatterns = draft.patterns
//                .filterNot { it.id == configId }
//                .mapIndexed { index, pattern ->
//                    pattern.copy(order = index)
//                }
//
//            draft.copy(
//                patterns = updatedPatterns
//            )
//        }
//    }
//
//    fun copyPattern(configId: Long): Long? {
//        val newId = generateConfigId()
//
//        _draft.update { draft ->
//            val source = draft.patterns.find { it.id == configId }
//                ?: return@update draft
//
//            val copiedPattern = source.copy(
//                id = newId,
//                order = draft.patterns.size
//            )
//
//            draft.copy(
//                patterns = draft.patterns + copiedPattern
//            )
//        }
//
//        return newId
//    }
//
//    private fun generateConfigId(): Long {
//        return System.currentTimeMillis()
//    }
//
//    fun movePattern(
//        configId: Long,
//        direction: MoveDirection
//    ) {
//        _draft.update { draft ->
//            val currentIndex = draft.patterns.indexOfFirst {
//                it.id == configId
//            }
//
//            if (currentIndex == -1) {
//                return@update draft
//            }
//
//            val newIndex = when (direction) {
//                MoveDirection.UP -> currentIndex - 1
//                MoveDirection.DOWN -> currentIndex + 1
//            }
//
//            // Już na początku / końcu listy
//            if (newIndex !in draft.patterns.indices) {
//                return@update draft
//            }
//
//            val updatedPatterns = draft.patterns.toMutableList()
//
//            // Zamiana miejscami
//            val temp = updatedPatterns[currentIndex]
//            updatedPatterns[currentIndex] = updatedPatterns[newIndex]
//            updatedPatterns[newIndex] = temp
//
//            // Aktualizacja kolejności
//            val reorderedPatterns = updatedPatterns.mapIndexed { index, pattern ->
//                pattern.copy(order = index)
//            }
//
//            draft.copy(
//                patterns = reorderedPatterns
//            )
//        }
//    }
//
//    fun setPatternEnabled(
//        configId: Long,
//        enabled: Boolean
//    ) {
//        _draft.update { draft ->
//            draft.copy(
//                patterns = draft.patterns.map { pattern ->
//                    if (pattern.id == configId) {
//                        pattern.copy(
//                            isEnabled = enabled
//                        )
//                    } else {
//                        pattern
//                    }
//                }
//            )
//        }
//    }
//
//    fun getPatternConfig(configId: Long): LearningStepsPatternConfigDraft? {
//        return _draft.value.patterns.find { it.id == configId }
//    }
//}

@HiltViewModel
class LearningStepsPatternsScreenViewModel @Inject constructor() : ViewModel() {

    private val _patternsDraft = MutableStateFlow<List<LearningStepsPatternConfigDraft>>(emptyList())
    val patternsDraft: StateFlow<List<LearningStepsPatternConfigDraft>> = _patternsDraft.asStateFlow()

    fun addPattern(
        pattern: PatternItem,
        width: PatternWidth,
        patternColor: ColorOption?,
        writingColor: ColorOption?,
        backgroundColor: ColorOption?,
        patternVariety: Boolean
    ): Long {
        val newId = generateConfigId()

        _patternsDraft.update { patterns ->
            val newPattern = LearningStepsPatternConfigDraft(
                id = newId,
                pattern = pattern,
                width = width,
                patternColor = patternColor,
                writingColor = writingColor,
                backgroundColor = backgroundColor,
                patternVariety = patternVariety,
                order = patterns.size
            )

            patterns + newPattern
        }

        return newId
    }

    fun updatePattern(
        configId: Long,
        patternItem: PatternItem,
        width: PatternWidth,
        patternColor: ColorOption?,
        writingColor: ColorOption?,
        backgroundColor: ColorOption?,
        patternVariety: Boolean
    ) {
        _patternsDraft.update { patterns ->
            patterns.map { config ->
                if (config.id == configId) {
                    config.copy(
                        pattern = patternItem,
                        width = width,
                        patternColor = patternColor,
                        writingColor = writingColor,
                        backgroundColor = backgroundColor,
                        patternVariety = patternVariety
                    )
                } else {
                    config
                }
            }
        }
    }

    fun deletePattern(configId: Long) {
        _patternsDraft.update { patterns ->
            patterns
                .filterNot { it.id == configId }
                .mapIndexed { index, pattern ->
                    pattern.copy(order = index)
                }
        }
    }

    fun copyPattern(configId: Long): Long? {
        val source = _patternsDraft.value.find {
            it.id == configId
        } ?: return null

        val newId = generateConfigId()

        val copiedPattern = source.copy(
            id = newId,
            order = _patternsDraft.value.size
        )

        _patternsDraft.update {
            it + copiedPattern
        }

        return newId
    }

    fun movePattern(
        configId: Long,
        direction: MoveDirection
    ) {
        _patternsDraft.update { patterns ->

            val currentIndex = patterns.indexOfFirst {
                it.id == configId
            }

            if (currentIndex == -1) {
                return@update patterns
            }

            val newIndex = when (direction) {
                MoveDirection.UP -> currentIndex - 1
                MoveDirection.DOWN -> currentIndex + 1
            }

            if (newIndex !in patterns.indices) {
                return@update patterns
            }

            val updatedPatterns = patterns.toMutableList()

            val temp = updatedPatterns[currentIndex]
            updatedPatterns[currentIndex] = updatedPatterns[newIndex]
            updatedPatterns[newIndex] = temp

            updatedPatterns.mapIndexed { index, pattern ->
                pattern.copy(order = index)
            }
        }
    }

    fun setPatternEnabled(
        configId: Long,
        enabled: Boolean
    ) {
        _patternsDraft.update { patterns ->
            patterns.map { pattern ->
                if (pattern.id == configId) {
                    pattern.copy(
                        isEnabled = enabled
                    )
                } else {
                    pattern
                }
            }
        }
    }

    fun getPatternConfig(
        configId: Long
    ): LearningStepsPatternConfigDraft? {
        return _patternsDraft.value.find {
            it.id == configId
        }
    }

    private fun generateConfigId(): Long {
        return System.currentTimeMillis()
    }
}
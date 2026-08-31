package com.example.friendlylines.therapist_app.ui.configuration.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import com.example.friendlylines.therapist_app.ui.materials.models.toPatternDrawing
import com.example.shared.data.repositories.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningStepsPatternConfigScreenViewModel @Inject constructor(
    private val patternRepository: PatternRepository
) : ViewModel() {

    private val _pattern = MutableStateFlow<PatternItem?>(null)
    val pattern: StateFlow<PatternItem?> = _pattern.asStateFlow()

    fun loadPattern(patternId: Long) {
        viewModelScope.launch {
            val patternWithStrokes = patternRepository.getPattern(patternId)

            _pattern.value = patternWithStrokes?.let {
                PatternItem(
                    pattern = it.pattern,
                    drawing = it.toPatternDrawing()
                )
            }
        }
    }
}
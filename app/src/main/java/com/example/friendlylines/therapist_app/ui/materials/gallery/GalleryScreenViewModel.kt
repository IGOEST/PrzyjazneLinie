package com.example.friendlylines.therapist_app.ui.materials.gallery

import android.util.Log
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
class GalleryScreenViewModel @Inject constructor(
    private val patternRepository: PatternRepository
) : ViewModel() {

    private val _patterns = MutableStateFlow<List<PatternItem>>(emptyList())

    val patterns: StateFlow<List<PatternItem>> = _patterns.asStateFlow()

    fun initializePatterns() {
        viewModelScope.launch {
            patternRepository.insertExamplePatterns()
            loadPatterns()
        }
    }

    private suspend fun loadPatterns() {
        val patterns = patternRepository.getAllPatterns()

        _patterns.value = patterns.mapNotNull { pattern ->
            val patternWithStrokes = patternRepository.getPattern(pattern.id)

            patternWithStrokes?.let {
                PatternItem(
                    pattern = pattern,
                    drawing = it.toPatternDrawing()
                )
            }
        }
    }

    fun deletePattern(patternId: Long) {
        viewModelScope.launch {
            patternRepository.deletePattern(patternId)
            loadPatterns()
        }
    }

    fun printPatterns() {
        viewModelScope.launch {
            val patterns = patternRepository.getAllPatterns()

            patterns.forEach { pattern ->
                Log.d("PATTERN_DB", pattern.toString())
            }
        }
    }
}
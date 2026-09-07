package com.example.friendlylines.therapist_app.ui.materials.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
//import com.example.friendlylines.therapist_app.ui.materials.models.toPatternDrawing
import com.example.shared.data.repositories.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.shared.data.models.PatternItem
import com.example.shared.data.models.toPatternDrawing

@HiltViewModel
class GalleryScreenViewModel @Inject constructor(
    private val patternRepository: PatternRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryScreenState())

    val state: StateFlow<GalleryScreenState> = _state.asStateFlow()

    init {
        initializePatterns()
    }

    fun onEvent(event: GalleryScreenEvent) {
        when (event) {
            is GalleryScreenEvent.HideExamplePatternsChanged -> {
                _state.update {
                    it.copy(
                        hideExamplePatterns = event.hide
                    )
                }
            }

            is GalleryScreenEvent.DeletePatternClicked -> {
                _state.update {
                    it.copy(
                        patternToDelete = event.pattern
                    )
                }
            }

            is GalleryScreenEvent.DeletePatternConfirmed -> {
                deletePattern()
            }

            is GalleryScreenEvent.DeletePatternDismissed -> {
                _state.update {
                    it.copy(
                        patternToDelete = null
                    )
                }
            }

            is GalleryScreenEvent.NewPatternReceived -> {
                viewModelScope.launch {
                    loadPatterns()

                    _state.update {
                        it.copy(
                            scrollToPatternId = event.patternId
                        )
                    }
                }
            }

            is GalleryScreenEvent.ScrollToPattern -> {
                _state.update {
                    it.copy(
                        scrollToPatternId = null
                    )
                }
            }
        }
    }

    private fun initializePatterns() {
        viewModelScope.launch {
            patternRepository.insertExamplePatterns()
            loadPatterns()
        }
    }

    private suspend fun loadPatterns() {
        val patterns = patternRepository.getAllPatterns()

        val patternItems = patterns.mapNotNull { pattern ->
            val patternWithStrokes = patternRepository.getPattern(pattern.id)

            patternWithStrokes?.let {
                PatternItem(
                    pattern = pattern,
                    drawing = it.toPatternDrawing()
                )
            }
        }

        _state.update {
            it.copy(
                patterns = patternItems
            )
        }
    }

    private fun deletePattern() {
        val pattern = _state.value.patternToDelete ?: return

        viewModelScope.launch {
            patternRepository.deletePattern(pattern.pattern.id)
            loadPatterns()

            _state.update {
                it.copy(
                    patternToDelete = null
                )
            }
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

//@HiltViewModel
//class GalleryScreenViewModel @Inject constructor(
//    private val patternRepository: PatternRepository
//) : ViewModel() {
//
//    private val _patterns = MutableStateFlow<List<PatternItem>>(emptyList())
//
//    val patterns: StateFlow<List<PatternItem>> = _patterns.asStateFlow()
//
//    fun initializePatterns() {
//        viewModelScope.launch {
//            patternRepository.insertExamplePatterns()
//            loadPatterns()
//        }
//    }
//
//    private suspend fun loadPatterns() {
//        val patterns = patternRepository.getAllPatterns()
//
//        _patterns.value = patterns.mapNotNull { pattern ->
//            val patternWithStrokes = patternRepository.getPattern(pattern.id)
//
//            patternWithStrokes?.let {
//                PatternItem(
//                    pattern = pattern,
//                    drawing = it.toPatternDrawing()
//                )
//            }
//        }
//    }
//
//    fun deletePattern(patternId: Long) {
//        viewModelScope.launch {
//            patternRepository.deletePattern(patternId)
//            loadPatterns()
//        }
//    }
//
//    fun printPatterns() {
//        viewModelScope.launch {
//            val patterns = patternRepository.getAllPatterns()
//
//            patterns.forEach { pattern ->
//                Log.d("PATTERN_DB", pattern.toString())
//            }
//        }
//    }
//}
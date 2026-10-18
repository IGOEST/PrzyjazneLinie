package com.example.friendlylines.therapist_app.ui.materials.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private suspend fun refreshGallery() {
        try {
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
                    patterns = patternItems,
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun loadPatterns() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            try {
                refreshGallery()

                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }

                throw e
            }
        }
    }

    private fun deletePattern() {
        val pattern = _state.value.patternToDelete ?: return

        viewModelScope.launch {
            patternRepository.deletePattern(pattern.pattern.id)
            refreshGallery()

            _state.update {
                it.copy(
                    patternToDelete = null
                )
            }
        }
    }
}
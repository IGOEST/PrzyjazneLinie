package com.example.friendlylines.therapist_app.ui.materials.create_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendlylines.therapist_app.ui.materials.models.toNormalizedStroke
import com.example.shared.data.entities.PatternWithStrokes
import com.example.shared.data.models.Point
import com.example.shared.data.repositories.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import javax.inject.Inject
import com.example.friendlylines.therapist_app.ui.main.ExitDestination

@HiltViewModel
class CreatePatternScreenViewModel @Inject constructor(
    private val patternRepository: PatternRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePatternScreenState())

    val state: StateFlow<CreatePatternScreenState> = _state.asStateFlow()

    fun onEvent(event: CreatePatternScreenEvent) {
        when (event) {
            is CreatePatternScreenEvent.SmoothingEnabledChanged -> {
                _state.update {
                    it.copy(
                        smoothingEnabled = event.enabled
                    )
                }
            }

            is CreatePatternScreenEvent.StraightLineEnabledChanged -> {
                _state.update {
                    it.copy(
                        straightLineEnabled = event.enabled
                    )
                }
            }

            CreatePatternScreenEvent.ClearDrawingClicked -> {
                // Obsługa czyszczenia rysunku pozostaje w Compose,
                // ponieważ strokes/currentStroke są lokalnym stanem canvasu.
            }

            CreatePatternScreenEvent.SaveClicked -> {
                _state.update {
                    it.copy(
                        showSaveDialog = true
                    )
                }
            }

            is CreatePatternScreenEvent.PatternNameChanged -> {
                _state.update {
                    it.copy(
                        patternName = event.name,
                        patternNameError = null
                    )
                }
            }

            CreatePatternScreenEvent.PatternBlankName -> {
                _state.update {
                    it.copy(
                        patternNameError = PatternNameError.BLANK
                    )
                }
            }

            CreatePatternScreenEvent.PatternNameExists -> {
                _state.update {
                    it.copy(
                        patternNameError = PatternNameError.EXISTS
                    )
                }
            }

            CreatePatternScreenEvent.SaveDismissed -> {
                _state.update {
                    it.copy(
                        showSaveDialog = false,
                        patternName = "",
                        patternNameError = null
                    )
                }
            }

            is CreatePatternScreenEvent.SaveConfirmed -> {
                savePatternViewModel(
                    strokes = event.strokes,
                    isComplex = event.isComplex,
                    createdAt = event.createdAt
                )
            }

            CreatePatternScreenEvent.SaveSuccessHandled -> {
                _state.update {
                    it.copy(
                        savedPatternId = null
                    )
                }
            }

            CreatePatternScreenEvent.ExitRequested -> {
                _state.update {
                    it.copy(
                        showExitDialog = true,
                        exitDestination = ExitDestination.PREVIOUS
                    )
                }
            }

            is CreatePatternScreenEvent.ExitToDestination -> {
                _state.update {
                    it.copy(
                        showExitDialog = true,
                        exitDestination = event.destination
                    )
                }
            }

            CreatePatternScreenEvent.ExitConfirmed -> {
                _state.update {
                    it.copy(
                        showExitDialog = false
                    )
                }
            }

            CreatePatternScreenEvent.ExitDismissed -> {
                _state.update {
                    it.copy(
                        showExitDialog = false,
                        exitDestination = null
                    )
                }
            }

            CreatePatternScreenEvent.ExitNavigationHandled -> {
                _state.update {
                    it.copy(
                        exitDestination = null
                    )
                }
            }
        }
    }

    private fun savePatternViewModel(
        strokes: List<List<Point>>,
        isComplex: Boolean,
        createdAt: Long
    ) {
        val name = _state.value.patternName.trim()

        if (name.isBlank()) {
            _state.update {
                it.copy(
                    patternNameError = PatternNameError.BLANK
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val exists = patternRepository.patternExistsByName(name)

                if (exists) {
                    _state.update {
                        it.copy(
                            patternNameError = PatternNameError.EXISTS
                        )
                    }
                    return@launch
                }

                val patternId = patternRepository.savePattern(
                    name = name,
                    isExample = false,
                    isComplex = isComplex,
                    createdAt = createdAt,
                    strokes = strokes,
                    smoothingEnabled = _state.value.smoothingEnabled
                )

                _state.update {
                    it.copy(
                        showSaveDialog = false,
                        patternName = "",
                        patternNameError = null,
                        savedPatternId = patternId
                    )
                }
            } catch (e: Exception) {
                // Zachowujemy obecne zachowanie:
                // onError było puste, więc tutaj również
                // nie pokazujemy żadnego błędu użytkownikowi.
            }
        }
    }

    fun savePattern(
        name: String,
        isExample: Boolean,
        isComplex: Boolean,
        createdAt: Long,
        strokes: List<List<Point>>,
        smoothingEnabled: Boolean,
        onSuccess: (Long) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val patternId = patternRepository.savePattern(
                    name = name,
                    isExample = isExample,
                    isComplex = isComplex,
                    createdAt = createdAt,
                    strokes = strokes,
                    smoothingEnabled = smoothingEnabled
                )

                onSuccess(patternId)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun getPattern(
        patternId: Long,
        onResult: (PatternWithStrokes?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val pattern = patternRepository.getPattern(patternId)
                onResult(pattern)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun checkPatternName(
        name: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val exists = patternRepository.patternExistsByName(name)
                onResult(exists)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}

//@HiltViewModel
//class CreatePatternScreenViewModel @Inject constructor(
//    private val patternRepository: PatternRepository
//) : ViewModel() {
//
//    fun savePattern(
//        name: String,
//        isExample: Boolean,
//        isComplex: Boolean,
//        createdAt: Long,
//        strokes: List<List<Point>>,
//        smoothingEnabled: Boolean,
//        onSuccess: (Long) -> Unit ={},
//        onError: (Throwable) -> Unit = {}
//    ) {
//        viewModelScope.launch {
//            try {
//                val patternId = patternRepository.savePattern(
//                    name = name,
//                    isExample = isExample,
//                    isComplex = isComplex,
//                    createdAt = createdAt,
//                    strokes = strokes,
//                    smoothingEnabled = smoothingEnabled
//                )
//                onSuccess(patternId)
//            } catch (e: Exception) {
//                onError(e)
//            }
//        }
//    }
//
//    fun getPattern(
//        patternId: Long,
//        onResult: (PatternWithStrokes?) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val pattern = patternRepository.getPattern(patternId)
//                onResult(pattern)
//            } catch (e: Exception) {
//                onResult(null)
//            }
//        }
//    }
//
//    fun checkPatternName(
//        name: String,
//        onResult: (Boolean) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val exists = patternRepository.patternExistsByName(name)
//                onResult(exists)
//            } catch (e: Exception) {
//                onResult(false)
//            }
//        }
//    }
//}
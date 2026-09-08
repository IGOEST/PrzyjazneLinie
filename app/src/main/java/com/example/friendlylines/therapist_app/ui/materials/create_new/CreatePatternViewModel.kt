//package com.example.friendlylines.therapist_app.ui.materials.create_new
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.shared.data.entities.PatternWithStrokes
//import com.example.shared.data.models.Point
//import com.example.shared.data.repositories.PatternRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class CreatePatternViewModel @Inject constructor(
//    private val patternRepository: PatternRepository
//) : ViewModel() {
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
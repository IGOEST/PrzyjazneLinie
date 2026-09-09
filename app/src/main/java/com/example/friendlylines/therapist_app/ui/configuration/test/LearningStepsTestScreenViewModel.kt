package com.example.friendlylines.therapist_app.ui.configuration.test

import androidx.lifecycle.ViewModel
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsTestDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// ViewModel responsible only for the test tab UI state
// (the actual saving is handled by LearningStepsSettingsViewModel)
class LearningStepsTestScreenViewModel @Inject constructor() : ViewModel() {

    private val _draft = MutableStateFlow(
        LearningStepsTestDraft(id = 0)
    )

    val draft: StateFlow<LearningStepsTestDraft> =
        _draft.asStateFlow()

    fun setRepetitions(value: Int) {
        _draft.update {
            it.copy(repetitions = value)
        }
    }

    fun setTimeLimit(value: Int) {
        _draft.update {
            it.copy(timeLimit = value)
        }
    }

    fun setAccuracyLevel(value: AccuracyLevel) {
        _draft.update {
            it.copy(accuracyLevel = value)
        }
    }
}
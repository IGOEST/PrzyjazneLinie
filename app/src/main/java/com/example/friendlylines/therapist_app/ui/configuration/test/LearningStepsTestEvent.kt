package com.example.friendlylines.therapist_app.ui.configuration.test

import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningEvent
import com.example.shared.data.drafts.AccuracyLevel

sealed class LearningStepsTestEvent {
    data class SetRepetitions(val value: Int) : LearningStepsTestEvent()
    data class SetTimeLimit(val value: Int) : LearningStepsTestEvent()
    data class SetAccuracyLevel(val value: AccuracyLevel) : LearningStepsTestEvent()
}
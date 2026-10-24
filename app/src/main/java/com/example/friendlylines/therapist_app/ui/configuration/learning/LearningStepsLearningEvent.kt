package com.example.friendlylines.therapist_app.ui.configuration.learning

import com.example.shared.data.drafts.AccuracyLevel

sealed class LearningStepsLearningEvent {
    data class SetRepetitions(val value: Int) : LearningStepsLearningEvent()
    data class SetAttempts(val value: Int) : LearningStepsLearningEvent()
    data class SetTimeLimit(val value: Int) : LearningStepsLearningEvent()
    data class SetAccuracyLevel(val value: AccuracyLevel) : LearningStepsLearningEvent()
    data class SetStartingPointEnabled(val enabled: Boolean) : LearningStepsLearningEvent()
    data class SetRandomPatternOrder(val enabled: Boolean) : LearningStepsLearningEvent()
}
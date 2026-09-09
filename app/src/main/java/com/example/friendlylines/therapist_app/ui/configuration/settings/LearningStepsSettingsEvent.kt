package com.example.friendlylines.therapist_app.ui.configuration.settings

import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningEvent
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsEvent
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestEvent
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft

sealed class LearningStepsSettingsEvent {
    data class Patterns(val event: LearningStepsPatternsEvent): LearningStepsSettingsEvent()
    data class Learning(val event: LearningStepsLearningEvent): LearningStepsSettingsEvent()
    data class Test(val event: LearningStepsTestEvent): LearningStepsSettingsEvent()
    data object ExitRequested : LearningStepsSettingsEvent()
    data class ExitToDestination(val destination: ExitDestination) : LearningStepsSettingsEvent()
    data object ExitConfirmed : LearningStepsSettingsEvent()
    data object ExitDismissed : LearningStepsSettingsEvent()
    data object ExitNavigationHandled : LearningStepsSettingsEvent()
}
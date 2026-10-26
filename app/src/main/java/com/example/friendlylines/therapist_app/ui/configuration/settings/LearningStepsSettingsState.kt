package com.example.friendlylines.therapist_app.ui.configuration.settings

import com.example.friendlylines.therapist_app.components.NameError
import com.example.friendlylines.therapist_app.navigation.ExitDestination
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft
import com.example.shared.data.drafts.LearningStepsReinforcementsDraft
import com.example.shared.data.drafts.LearningStepsTestDraft

data class LearningStepsSettingsState (
    val stepName: String = "",
    val patternState: LearningStepsPatternsDraft = LearningStepsPatternsDraft(),
    val learningState: LearningStepsLearningDraft = LearningStepsLearningDraft(),
    val testState: LearningStepsTestDraft = LearningStepsTestDraft(),
    val reinforcementsState: LearningStepsReinforcementsDraft = LearningStepsReinforcementsDraft(),
    val showExitDialog: Boolean = false,
    val exitDestination: ExitDestination? = null,
    val savedLearningStepId: Long? = null,
    val stepNameError: NameError? = null,
    val showSaveDialog: Boolean = false,
    val isLoading: Boolean = false
)
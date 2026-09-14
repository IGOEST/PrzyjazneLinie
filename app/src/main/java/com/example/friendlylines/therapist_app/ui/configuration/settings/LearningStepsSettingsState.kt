package com.example.friendlylines.therapist_app.ui.configuration.settings

import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepNameError
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft
import com.example.shared.data.drafts.LearningStepsTestDraft

data class LearningStepsSettingsState (
    val stepName: String = "",
    val patternState: LearningStepsPatternsDraft = LearningStepsPatternsDraft(),
    val learningState: LearningStepsLearningDraft = LearningStepsLearningDraft(),
    val testState: LearningStepsTestDraft = LearningStepsTestDraft(),
    val showExitDialog: Boolean = false,
    val exitDestination: ExitDestination? = null,
    val savedLearningStepId: Long? = null,
    val stepNameError: LearningStepNameError? = null
)
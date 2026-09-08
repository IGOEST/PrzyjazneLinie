package com.example.friendlylines.therapist_app.ui.configuration.settings

import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft

data class LearningStepsSettingsState (
    val patternState: LearningStepsPatternsDraft = LearningStepsPatternsDraft(),
    val learningState: LearningStepsLearningDraft = LearningStepsLearningDraft(),
    val showExitDialog: Boolean = false,
    val exitDestination: ExitDestination? = null
)
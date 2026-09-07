package com.example.friendlylines.therapist_app.ui.configuration.list

import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft

data class LearningStepsDraft(
    val id: Long,
    val name: String = "",
    val patterns: List<LearningStepsPatternConfigDraft> = emptyList(),
)
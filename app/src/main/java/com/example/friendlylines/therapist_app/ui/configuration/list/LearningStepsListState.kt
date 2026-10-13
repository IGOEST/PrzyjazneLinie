package com.example.friendlylines.therapist_app.ui.configuration.list

import com.example.shared.data.entities.LearningStepEntity

data class LearningStepsListState(
    val learningSteps: List<LearningStepEntity> = emptyList(),
    val isLoading: Boolean = false,
    val copiedLearningStepId: Long? = null,
    val searchQuery: String = "",
)
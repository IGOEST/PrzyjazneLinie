package com.example.friendlylines.therapist_app.navigation

import kotlinx.serialization.Serializable

sealed class TherapistNavRoutes {

    @Serializable
    data object Home : TherapistNavRoutes()

    @Serializable
    data object Gallery: TherapistNavRoutes()

    @Serializable
    data object PatternCreate: TherapistNavRoutes()

    @Serializable
    data object LearningStepsList: TherapistNavRoutes()

    @Serializable
    data class LearningStepsCreate(val stepId: Long? = null) : TherapistNavRoutes()

    @Serializable
    data class LearningStepsPatternAdd(val stepId: Long? = null, val configId: Long? = null, val patternId: Long? = null, val order: Int? = null) : TherapistNavRoutes()

    @Serializable
    data class LearningStepsPatternConfig(val stepId: Long? = null, val patternId: Long, val configId: Long? = null, val order: Int? = null) : TherapistNavRoutes()
}
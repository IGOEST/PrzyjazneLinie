package com.example.friendlylines.therapist_app.ui.configuration.reinforcement

sealed class LearningStepsReinforcementsEvent {
    data class SetEnabledPraiseWords(val words: Set<String>, val enabled: Boolean) : LearningStepsReinforcementsEvent()
    data class SetEnabledAnimationThemes(val themes: Set<String>, val enabled: Boolean) : LearningStepsReinforcementsEvent()
    data class SetEndSessionAnimationEnabled(val enabled: Boolean) : LearningStepsReinforcementsEvent()
    data class SetEndSessionFanfareEnabled(val enabled: Boolean) : LearningStepsReinforcementsEvent()
}
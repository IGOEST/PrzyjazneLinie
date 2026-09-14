package com.example.friendlylines.therapist_app.ui.configuration.list

sealed class LearningStepsListEvent {
    data class ActiveChanged(val id: Long, val isActive: Boolean) : LearningStepsListEvent()
    data class ModeChanged(val id: Long, val isTest: Boolean) : LearningStepsListEvent()
    data class CopyClicked(val id: Long) : LearningStepsListEvent()
    data class DeleteClicked(val id: Long) : LearningStepsListEvent()
    data object CopiedLearningStepHandled : LearningStepsListEvent()
    data class SearchQueryChanged(val query: String) : LearningStepsListEvent()
}
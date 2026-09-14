package com.example.friendlylines.therapist_app.ui.configuration.summary

sealed class LearningStepsSummaryEvent {
    data object SaveClicked : LearningStepsSummaryEvent()
    data class NameChanged(val name: String) : LearningStepsSummaryEvent()
}
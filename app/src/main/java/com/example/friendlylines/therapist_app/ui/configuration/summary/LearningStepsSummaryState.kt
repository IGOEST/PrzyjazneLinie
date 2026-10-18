package com.example.friendlylines.therapist_app.ui.configuration.summary

data class LearningStepsSummaryState(
    val rows: List<SummaryRow>
)

data class SummaryRow(
    val label: String,
    val learningValue: String,
    val testValue: String
)
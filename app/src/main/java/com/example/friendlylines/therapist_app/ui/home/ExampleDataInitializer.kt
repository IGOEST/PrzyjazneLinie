package com.example.friendlylines.therapist_app.ui.main

import com.example.shared.data.repositories.LearningStepsRepository
import com.example.shared.data.repositories.PatternRepository
import jakarta.inject.Inject

class ExampleDataInitializer @Inject constructor(
    private val patternRepository: PatternRepository,
    private val learningStepsRepository: LearningStepsRepository
){
    suspend fun initialize() {
        patternRepository.insertExamplePatterns()
        learningStepsRepository.insertExampleLearningSteps()
    }
}
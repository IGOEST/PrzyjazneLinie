package com.example.shared.data.examples

import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.entities.LearningStepEntity

data class ExampleLearningStep (
    val key: String,
    val step: LearningStepEntity,
    val patternKeys: List<String>
)

object ExampleLearningSteps {
    fun create(): List<ExampleLearningStep> {
        return listOf(
            ExampleLearningStep(
                key = "easy",
                step = LearningStepEntity(
                    name = "Easy",
                    isActive = true,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learningRepetitions = 3,
                    learningAttempts = 2,
                    learningTimeLimit = 20,
                    learningAccuracyLevel = AccuracyLevel.EASY.name,
                    learningStartingPointEnabled = true,
                    learningRandomPatternOrder = true,

                    testRepetitions = 2,
                    testAttempts = 1,
                    testTimeLimit = 10,
                    testAccuracyLevel = AccuracyLevel.EASY.name,
                    testStartingPointEnabled = false,
                    testRandomPatternOrder = false
                ),
                patternKeys = listOf("square")
            ),
            ExampleLearningStep(
                key = "medium",
                step = LearningStepEntity(
                    name = "Medium",
                    isActive = false,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learningRepetitions = 3,
                    learningAttempts = 2,
                    learningTimeLimit = 20,
                    learningAccuracyLevel = AccuracyLevel.MEDIUM.name,
                    learningStartingPointEnabled = true,
                    learningRandomPatternOrder = true,

                    testRepetitions = 2,
                    testAttempts = 1,
                    testTimeLimit = 10,
                    testAccuracyLevel = AccuracyLevel.MEDIUM.name,
                    testStartingPointEnabled = false,
                    testRandomPatternOrder = false
                ),
                patternKeys = listOf("square", "triangle")
            ),
            ExampleLearningStep(
                key = "hard",
                step = LearningStepEntity(
                    name = "Hard",
                    isActive = false,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learningRepetitions = 1,
                    learningAttempts = 2,
                    learningTimeLimit = 20,
                    learningAccuracyLevel = AccuracyLevel.HARD.name,
                    learningStartingPointEnabled = true,
                    learningRandomPatternOrder = true,

                    testRepetitions = 1,
                    testAttempts = 1,
                    testTimeLimit = 10,
                    testAccuracyLevel = AccuracyLevel.HARD.name,
                    testStartingPointEnabled = false,
                    testRandomPatternOrder = false
                ),
                patternKeys = listOf("square", "triangle", "circle")
            )
        )
    }
}
package com.example.shared.data.examples

import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsTestDraft
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
                    name = "easy",
                    isActive = true,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learning = LearningStepsLearningDraft(
                        repetitions = 3,
                        attempts = 2,
                        timeLimit = 20,
                        accuracyLevel = AccuracyLevel.EASY,
                        startingPointEnabled = true,
                        randomPatternOrder = true
                    ),
                    test = LearningStepsTestDraft(
                        overridesLearning = true,
                        repetitions = 2,
                        attempts = 1,
                        timeLimit = 10,
                        accuracyLevel = AccuracyLevel.EASY,
                        startingPointEnabled = false,
                        randomPatternOrder = false
                    )
                ),
                patternKeys = listOf("vertical_line", "horizontal_line")
            ),
            ExampleLearningStep(
                key = "medium",
                step = LearningStepEntity(
                    name = "medium",
                    isActive = false,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learning = LearningStepsLearningDraft(
                        repetitions = 3,
                        attempts = 2,
                        timeLimit = 20,
                        accuracyLevel = AccuracyLevel.MEDIUM,
                        startingPointEnabled = true,
                        randomPatternOrder = true
                    ),
                    test = LearningStepsTestDraft(
                        overridesLearning = true,
                        repetitions = 2,
                        attempts = 1,
                        timeLimit = 10,
                        accuracyLevel = AccuracyLevel.MEDIUM,
                        startingPointEnabled = false,
                        randomPatternOrder = false
                    )
                ),
                patternKeys = listOf("diagonal_line_down", "diagonal_line_up", "curve_line")
            ),
            ExampleLearningStep(
                key = "hard",
                step = LearningStepEntity(
                    name = "hard",
                    isActive = false,
                    isLearning = true,
                    isTest = false,
                    isExample = true,

                    learning = LearningStepsLearningDraft(
                        repetitions = 1,
                        attempts = 2,
                        timeLimit = 20,
                        accuracyLevel = AccuracyLevel.HARD,
                        startingPointEnabled = true,
                        randomPatternOrder = true
                    ),
                    test = LearningStepsTestDraft(
                        overridesLearning = true,
                        repetitions = 1,
                        attempts = 1,
                        timeLimit = 10,
                        accuracyLevel = AccuracyLevel.HARD,
                        startingPointEnabled = false,
                        randomPatternOrder = false
                    )
                ),
                patternKeys = listOf("square", "triangle", "circle", "rectangle", "ellipse")
            )
        )
    }
}
package com.example.shared.data.examples

import com.example.shared.data.entities.LearningStepPatternEntity
import com.example.shared.data.drafts.PatternWidth

object ExamplePatternConfiguration {
    fun create(
        patternId: Long,
        order: Int
    ): LearningStepPatternEntity {
        return LearningStepPatternEntity(
            learningStepId = 0L,
            patternId = patternId,
            width = PatternWidth.MEDIUM.name,
            patternColor = "blue",
            writingColor = "red",
            backgroundColor = "white",
            patternVariety = false,
            order = order,
            isEnabled = true,
            isTest = true,
            isLearning = true
        )
    }
}
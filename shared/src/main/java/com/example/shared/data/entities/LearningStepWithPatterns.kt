package com.example.shared.data.entities

import androidx.room3.Embedded
import androidx.room3.Relation

data class LearningStepWithPatterns(
    @Embedded
    val learningStep: LearningStepEntity,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["learningStepId"]
    )
    val patterns: List<LearningStepPatternEntity>
)
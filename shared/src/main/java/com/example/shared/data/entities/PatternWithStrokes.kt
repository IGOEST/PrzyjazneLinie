package com.example.shared.data.entities

import androidx.room3.Embedded
import androidx.room3.Relation

data class PatternWithStrokes(
    @Embedded
    val pattern: PatternEntity,
    @Relation(
        entity = StrokeEntity::class,
        parentColumns = ["id"],
        entityColumns = ["patternId"]
    )
    val strokes: List<StrokeWithPoints>
)
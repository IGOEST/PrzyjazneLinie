package com.example.shared.data.entities

import androidx.room3.Embedded
import androidx.room3.Relation
data class StrokeWithPoints(
    @Embedded
    val stroke: StrokeEntity,
    @Relation(
        parentColumns = ["id"],
        entityColumns = ["strokeId"]
    )
    val points: List<PointEntity>
)
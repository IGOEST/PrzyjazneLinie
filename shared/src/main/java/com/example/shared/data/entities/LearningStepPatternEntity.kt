package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

// Represents a pattern assigned to a specific learning step
// Stores configuration of a pattern inside a learning step
// Actual pattern data is stored in PatternEntity
@Entity(
    tableName = "learning_step_patterns",
    foreignKeys = [
        ForeignKey(
            entity = LearningStepEntity::class,
            parentColumns = ["id"],
            childColumns = ["learningStepId"],
            onDelete = ForeignKey.CASCADE // deletes the configuration when the learning step is deleted
        ),
        ForeignKey(
            entity = PatternEntity::class,
            parentColumns = ["id"],
            childColumns = ["patternId"],
            onDelete = ForeignKey.CASCADE // deletes the configuration when the referenced pattern is deleted
        )
    ],
    indices = [
        Index("learningStepId"),
        Index("patternId")
    ]
)
data class LearningStepPatternEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val learningStepId: Long,   // identifies the learning step this pattern belongs to
    val patternId: Long,    // references the actual pattern stored in PatternEntity

    // configuration of this pattern within the learning step
    val width: String,
    val patternColor: String?,
    val writingColor: String?,
    val backgroundColor: String?,
    val patternVariety: Boolean,

    val order: Int,
    val isEnabled: Boolean
)
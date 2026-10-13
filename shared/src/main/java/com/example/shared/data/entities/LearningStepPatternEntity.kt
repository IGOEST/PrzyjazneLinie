package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.ForeignKey.Companion.CASCADE
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "learning_step_patterns",
    foreignKeys = [
        ForeignKey(
            entity = LearningStepEntity::class,
            parentColumns = ["id"],
            childColumns = ["learningStepId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PatternEntity::class,
            parentColumns = ["id"],
            childColumns = ["patternId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["learningStepId"]),
        Index(value = ["patternId"])
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
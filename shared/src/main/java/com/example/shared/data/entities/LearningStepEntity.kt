package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.shared.data.drafts.AccuracyLevel

@Entity(
    tableName = "learning_steps"
)
data class LearningStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val isActive: Boolean = false,
    val isLearning: Boolean = true,
    val isTest: Boolean = false,

    // Learning settings
    val learningRepetitions: Int,
    val learningAttempts: Int,
    val learningTimeLimit: Int,
    val learningAccuracyLevel: String,
    val learningStartingPointEnabled: Boolean,
    val learningRandomPatternOrder: Boolean,

    // Test settings
    val testRepetitions: Int,
    val testAttempts: Int,
    val testTimeLimit: Int,
    val testAccuracyLevel: String,
    val testStartingPointEnabled: Boolean,
    val testRandomPatternOrder: Boolean
)
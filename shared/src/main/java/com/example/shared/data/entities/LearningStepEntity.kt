package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(
    tableName = "learning_steps"
)
data class LearningStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val activeStep: Boolean = false,

    // learning settings
    val repetitions: Int,
    val attempts: Int,
    val timeLimit: Int,
    val accuracyLevel: String,
    val startingPointEnabled: Boolean,
    val randomPatternOrder: Boolean,

    // test settings
    val testRepetitions: Int,
    val testTimeLimit: Int,
    val testAccuracyLevel: String
)
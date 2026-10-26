package com.example.shared.data.entities

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsReinforcementsDraft
import com.example.shared.data.drafts.LearningStepsTestDraft

@Entity(
    tableName = "learning_steps"
)
data class LearningStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val isActive: Boolean,
    val isLearning: Boolean = true,
    val isTest: Boolean = false,
    val isExample: Boolean,

    @Embedded(prefix = "learning_")
    val learning: LearningStepsLearningDraft = LearningStepsLearningDraft(),

    @Embedded(prefix = "test_")
    val test: LearningStepsTestDraft = LearningStepsTestDraft(),

    @Embedded(prefix = "reinforcements_")
    val reinforcements: LearningStepsReinforcementsDraft = LearningStepsReinforcementsDraft()
)
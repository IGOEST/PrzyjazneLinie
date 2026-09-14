package com.example.shared.data.daos

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.entities.LearningStepPatternEntity
import com.example.shared.data.entities.LearningStepWithPatterns

@Dao
interface LearningStepsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningStep(learningStep: LearningStepEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningStepPatterns(patterns: List<LearningStepPatternEntity>)

    @Transaction
    suspend fun insertLearningStepWithPatterns(
        learningStep: LearningStepEntity,
        patterns: List<LearningStepPatternEntity>
    ): Long {
        val learningStepId = insertLearningStep(learningStep)

        if (patterns.isNotEmpty()) {
            insertLearningStepPatterns(
                patterns = patterns.map {
                    it.copy(
                        learningStepId = learningStepId
                    )
                }
            )
        }

        return learningStepId
    }

    @Transaction
    @Query("SELECT * FROM learning_steps WHERE id = :stepId")
    suspend fun getLearningStepWithPatterns(stepId: Long): LearningStepWithPatterns?

    @Query("DELETE FROM learning_steps WHERE id = :stepId")
    suspend fun deleteLearningStep(stepId: Long)

    @Query("SELECT * FROM learning_steps ORDER BY id ASC")
    suspend fun getAll(): List<LearningStepEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM learning_steps WHERE name = :name AND id != :excludedId)")
    suspend fun existsLearningStepByName(name: String, excludedId: Long): Boolean

    @Query("UPDATE learning_steps SET isActive = false WHERE isActive = true")
    suspend fun deactivateAll()

    @Query("UPDATE learning_steps SET isActive = :isActive WHERE id = :id")
    suspend fun setActive(id: Long, isActive: Boolean)

    @Transaction
    suspend fun updateActiveStep(id: Long, isActive: Boolean) {
        if (isActive) {
            deactivateAll()
        }

        setActive(
            id = id,
            isActive = isActive
        )
    }

    @Query("UPDATE learning_steps SET isLearning = :isLearning, isTest = :isTest WHERE id = :id")
    suspend fun updateMode(id: Long, isLearning: Boolean, isTest: Boolean)

    @Update
    suspend fun updateLearningStep(learningStep: LearningStepEntity)

    @Query("DELETE FROM learning_step_patterns WHERE learningStepId = :learningStepId")
    suspend fun deleteLearningStepPatterns(learningStepId: Long)

    @Transaction
    suspend fun updateLearningStepWithPatterns(
        learningStep: LearningStepEntity,
        patterns: List<LearningStepPatternEntity>
    ) {
        updateLearningStep(learningStep)

        deleteLearningStepPatterns(
            learningStepId = learningStep.id
        )

        if (patterns.isNotEmpty()) {
            insertLearningStepPatterns(
                patterns.map {
                    it.copy(
                        id = 0L,
                        learningStepId = learningStep.id
                    )
                }
            )
        }
    }
}
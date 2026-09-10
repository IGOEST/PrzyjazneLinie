package com.example.shared.data.daos

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.shared.data.entities.LearningStepEntity

//Provides database operations for learning steps
@Dao
interface LearningStepDao {

    // inserts a new learning step or replaces an existing one with the same primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        learningStep: LearningStepEntity
    ): Long

    // returns a learning step by its id, returns null when the step does not exist
    @Query(
        """
        SELECT *
        FROM learning_steps
        WHERE id = :id
        """
    )
    suspend fun getById(
        id: Long
    ): LearningStepEntity?

    // returns all saved learning steps
    @Query(
        """
        SELECT *
        FROM learning_steps
        ORDER BY id ASC
        """
    )
    suspend fun getAll(): List<LearningStepEntity>

    // deletes a learning step by id
    @Query(
        """
        DELETE FROM learning_steps
        WHERE id = :id
        """
    )
    suspend fun deleteById(
        id: Long
    )

    // updating which step is active
    @Query(
        """
    UPDATE learning_steps
    SET activeStep = 0
    """
    )
    suspend fun deactivateAllSteps()

    @Query(
        """
    UPDATE learning_steps
    SET activeStep = 1
    WHERE id = :learningStepId
    """
    )
    suspend fun setActiveStep(
        learningStepId: Long
    )

    // updating mode (test or learning)
    @Query(
        """
    UPDATE learning_steps
    SET mode = :mode
    WHERE id = :learningStepId
    """
    )
    suspend fun updateMode(
        learningStepId: Long,
        mode: String
    )

    // for editing learning step
    @Query(
        """
    UPDATE learning_steps
    SET name = :name,
        repetitions = :repetitions,
        attempts = :attempts,
        timeLimit = :timeLimit,
        accuracyLevel = :accuracyLevel,
        startingPointEnabled = :startingPointEnabled,
        randomPatternOrder = :randomPatternOrder,
        testRepetitions = :testRepetitions,
        testTimeLimit = :testTimeLimit,
        testAccuracyLevel = :testAccuracyLevel
    WHERE id = :learningStepId
    """
    )
    suspend fun update(
        learningStepId: Long,
        name: String,
        repetitions: Int,
        attempts: Int,
        timeLimit: Int,
        accuracyLevel: String,
        startingPointEnabled: Boolean,
        randomPatternOrder: Boolean,
        testRepetitions: Int,
        testTimeLimit: Int,
        testAccuracyLevel: String
    )

    // to ensure uniqueness of each steps name
    @Query(
        """
    SELECT EXISTS(
        SELECT 1
        FROM learning_steps
        WHERE name = :name
        AND (:excludeId IS NULL OR id != :excludeId)
    )
    """
    )
    suspend fun existsByName(
        name: String,
        excludeId: Long?
    ): Boolean
}
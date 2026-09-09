package com.example.shared.data.daos

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.shared.data.entities.LearningStepPatternEntity

 // Provides database operations for patterns assigned to learning steps
// Responsible only for reading and writing data

@Dao
interface LearningStepPatternDao {

    // saves a pattern configuration assigned to a learning step
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        pattern: LearningStepPatternEntity
    ): Long

    // saves multiple pattern configurations at once.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        patterns: List<LearningStepPatternEntity>
    ): List<Long>

    // returns all patterns assigned to a learning step ordered
    @Query(
        """
        SELECT * 
        FROM learning_step_patterns
        WHERE learningStepId = :learningStepId
        ORDER BY `order` ASC
        """
    )
    suspend fun getForLearningStep(
        learningStepId: Long
    ): List<LearningStepPatternEntity>

    // deletes one pattern configuration
    @Query(
        """
        DELETE FROM learning_step_patterns
        WHERE id = :id
        """
    )
    suspend fun deleteById(
        id: Long
    )

    // deletes all pattern configurations belonging to a learning step
    @Query(
        """
        DELETE FROM learning_step_patterns
        WHERE learningStepId = :learningStepId
        """
    )
    suspend fun deleteForLearningStep(
        learningStepId: Long
    )
}
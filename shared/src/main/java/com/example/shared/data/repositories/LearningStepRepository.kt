package com.example.shared.data.repositories

import com.example.shared.data.daos.LearningStepPatternDao
import com.example.shared.data.daos.LearningStepDao
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.entities.LearningStepPatternEntity
import javax.inject.Inject


// Repository responsible for storing and reading learning step configuration (bridge between app and the database)

class LearningStepRepository @Inject constructor(
    private val learningStepDao: LearningStepDao,
    private val learningStepPatternDao: LearningStepPatternDao
) {

    // saves a learning step in the database
    suspend fun saveLearningStep(
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
    ): Long {

        val learningStep = LearningStepEntity(
            name = name,
            repetitions = repetitions,
            attempts = attempts,
            timeLimit = timeLimit,
            accuracyLevel = accuracyLevel,
            startingPointEnabled = startingPointEnabled,
            randomPatternOrder = randomPatternOrder,
            testRepetitions = testRepetitions,
            testTimeLimit = testTimeLimit,
            testAccuracyLevel = testAccuracyLevel
        )

        return learningStepDao.insert(learningStep)
    }

    // saves a pattern configuration assigned to a learning step
    suspend fun savePatternConfiguration(
        pattern: LearningStepPatternEntity
    ): Long {
        return learningStepPatternDao.insert(pattern)
    }

    // saves multiple pattern configurations assigned to a learning step
    suspend fun savePatternConfigurations(
        patterns: List<LearningStepPatternEntity>
    ): List<Long> {
        return learningStepPatternDao.insertAll(patterns)
    }

    // returns all pattern configurations belonging to a learning step
    suspend fun getPatternConfigurations(
        learningStepId: Long
    ): List<LearningStepPatternEntity> {
        return learningStepPatternDao.getForLearningStep(learningStepId)
    }

    // returns all saved learning steps
    suspend fun getAllLearningSteps(): List<LearningStepEntity> {
        return learningStepDao.getAll()
    }

    // returns one learning step by id
    suspend fun getLearningStep(
        learningStepId: Long
    ): LearningStepEntity? {
        return learningStepDao.getById(learningStepId)
    }

    // deletes all pattern configurations belonging to a learning step
    suspend fun deletePatternConfigurations(
        learningStepId: Long
    ) {
        learningStepPatternDao.deleteForLearningStep(learningStepId)
    }

    // deletes a complete learning step
    suspend fun deleteLearningStep(
        learningStepId: Long
    ) {
        learningStepDao.deleteById(learningStepId)
    }

    // updates only the test settings of an existing learning step
    suspend fun updateTestSettings(
        learningStepId: Long,
        testRepetitions: Int,
        testTimeLimit: Int,
        testAccuracyLevel: String
    ) {
        learningStepDao.updateTestSettings(
            learningStepId = learningStepId,
            testRepetitions = testRepetitions,
            testTimeLimit = testTimeLimit,
            testAccuracyLevel = testAccuracyLevel
        )
    }

    // mark the step active, others - inactive
    suspend fun setActiveStep(
        learningStepId: Long
    ) {
        learningStepDao.deactivateAllSteps()
        learningStepDao.setActiveStep(learningStepId)
    }

    // returns active learning step
    suspend fun getActiveStep(): LearningStepEntity {
        return learningStepDao.getActiveStep()
        
    // change the mode (test, learning)
    suspend fun updateLearningStepMode(
        learningStepId: Long,
        mode: String
    ) {
        learningStepDao.updateMode(
            learningStepId = learningStepId,
            mode = mode
        )
    }
}
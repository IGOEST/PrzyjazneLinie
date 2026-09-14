package com.example.shared.data.repositories

import com.example.shared.data.daos.LearningStepsDao
import com.example.shared.data.daos.PatternDao
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.drafts.LearningStepsDraft
import com.example.shared.data.drafts.LearningStepsLearningDraft
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft
import com.example.shared.data.drafts.LearningStepsTestDraft
import com.example.shared.data.drafts.PatternConfigOptions
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.entities.LearningStepPatternEntity
import com.example.shared.data.models.PatternItem
import com.example.shared.data.models.toPatternDrawing
import javax.inject.Inject

class LearningStepsRepository @Inject constructor(
    private val learningStepsDao: LearningStepsDao,
    private val patternRepository: PatternRepository
) {
    suspend fun saveLearningStep(draft: LearningStepsDraft): Long {

        val learningStepEntity = LearningStepEntity(
            name = draft.name,

            // Learning
            learningRepetitions = draft.learning.repetitions,
            learningAttempts = draft.learning.attempts,
            learningTimeLimit = draft.learning.timeLimit,
            learningAccuracyLevel = draft.learning.accuracyLevel.name,
            learningStartingPointEnabled = draft.learning.startingPointEnabled,
            learningRandomPatternOrder = draft.learning.randomPatternOrder,

            // Test
            testRepetitions = draft.test.repetitions,
            testAttempts = draft.test.attempts,
            testTimeLimit = draft.test.timeLimit,
            testAccuracyLevel = draft.test.accuracyLevel.name,
            testStartingPointEnabled = draft.test.startingPointEnabled,
            testRandomPatternOrder = draft.test.randomPatternOrder
        )

        val patternEntities = draft.patterns.patterns.map { patternDraft ->
            LearningStepPatternEntity(
                learningStepId = 0, // zostanie ustawione przez DAO
                patternId = patternDraft.pattern.pattern.id,
                width = patternDraft.width.name,
                patternColor = patternDraft.patternColor?.name,
                writingColor = patternDraft.writingColor?.name,
                backgroundColor = patternDraft.backgroundColor?.name,
                patternVariety = patternDraft.patternVariety,
                order = patternDraft.order,
                isEnabled = patternDraft.isEnabled
            )
        }

        return learningStepsDao.insertLearningStepWithPatterns(
            learningStep = learningStepEntity,
            patterns = patternEntities
        )
    }

        suspend fun getLearningStep(stepId: Long): LearningStepsDraft? {
        val data = learningStepsDao.getLearningStepWithPatterns(stepId) ?: return null
        val learningStep = data.learningStep

        val patternDrafts = data.patterns
            .sortedBy { it.order }
            .mapNotNull { patternConfig ->
                val patternWithStrokes = patternRepository.getPattern(
                    patternConfig.patternId
                ) ?: return@mapNotNull null

                LearningStepsPatternConfigDraft(
                    id = patternConfig.id,
                    pattern = PatternItem(
                        pattern = patternWithStrokes.pattern,
                        drawing = patternWithStrokes.toPatternDrawing()
                    ),
                    width = PatternWidth.valueOf(patternConfig.width),
                    patternColor = patternConfig.patternColor?.let { name ->
                        PatternConfigOptions.patternAndWriting.firstOrNull{ it.name == name }
                    },
                    writingColor = patternConfig.writingColor?.let { name ->
                        PatternConfigOptions.patternAndWriting.firstOrNull { it.name == name }
                    },
                    backgroundColor = patternConfig.backgroundColor?.let { name ->
                        PatternConfigOptions.background.firstOrNull { it.name == name }
                    },
                    patternVariety = patternConfig.patternVariety,
                    order = patternConfig.order,
                    isEnabled = patternConfig.isEnabled
                )
            }

        return LearningStepsDraft(
            id = learningStep.id,
            name = learningStep.name,
            patterns = LearningStepsPatternsDraft(
                patterns = patternDrafts
            ),
            learning = LearningStepsLearningDraft(
                repetitions = learningStep.learningRepetitions,
                attempts = learningStep.learningAttempts,
                timeLimit = learningStep.learningTimeLimit,
                accuracyLevel = AccuracyLevel.valueOf(
                    learningStep.learningAccuracyLevel
                ),
                startingPointEnabled = learningStep.learningStartingPointEnabled,
                randomPatternOrder = learningStep.learningRandomPatternOrder
            ),
            test = LearningStepsTestDraft(
                repetitions = learningStep.testRepetitions,
                attempts = learningStep.testAttempts,
                timeLimit = learningStep.testTimeLimit,
                accuracyLevel = AccuracyLevel.valueOf(
                    learningStep.testAccuracyLevel
                ),
                startingPointEnabled = learningStep.testStartingPointEnabled,
                randomPatternOrder = learningStep.testRandomPatternOrder
            )
        )
    }

    suspend fun getAllLearningSteps(): List<LearningStepEntity> {
        return learningStepsDao.getAll()
    }

    suspend fun updateLearningStepActive(id: Long, isActive: Boolean) {
        learningStepsDao.updateActiveStep(
            id = id,
            isActive = isActive
        )
    }

    suspend fun updateLearningStepMode(id: Long, isLearning: Boolean, isTest: Boolean) {
        learningStepsDao.updateMode(
            id = id,
            isLearning = isLearning,
            isTest = isTest
        )
    }

    suspend fun updateLearningStep(draft: LearningStepsDraft) {
        val existing = learningStepsDao.getLearningStepWithPatterns(draft.id) ?: return
        val current = existing.learningStep

        val learningStepEntity = LearningStepEntity(
            id = draft.id,
            name = draft.name,

            isActive = current.isActive,
            isLearning = current.isLearning,
            isTest = current.isTest,

            learningRepetitions = draft.learning.repetitions,
            learningAttempts = draft.learning.attempts,
            learningTimeLimit = draft.learning.timeLimit,
            learningAccuracyLevel = draft.learning.accuracyLevel.name,
            learningStartingPointEnabled = draft.learning.startingPointEnabled,
            learningRandomPatternOrder = draft.learning.randomPatternOrder,

            testRepetitions = draft.test.repetitions,
            testAttempts = draft.test.attempts,
            testTimeLimit = draft.test.timeLimit,
            testAccuracyLevel = draft.test.accuracyLevel.name,
            testStartingPointEnabled = draft.test.startingPointEnabled,
            testRandomPatternOrder = draft.test.randomPatternOrder
        )

        val patternEntities = draft.patterns.patterns.map { patternDraft ->
            LearningStepPatternEntity(
                id = 0L,
                learningStepId = draft.id,
                patternId = patternDraft.pattern.pattern.id,
                width = patternDraft.width.name,
                patternColor = patternDraft.patternColor?.name,
                writingColor = patternDraft.writingColor?.name,
                backgroundColor = patternDraft.backgroundColor?.name,
                patternVariety = patternDraft.patternVariety,
                order = patternDraft.order,
                isEnabled = patternDraft.isEnabled
            )
        }

        learningStepsDao.updateLearningStepWithPatterns(
            learningStep = learningStepEntity,
            patterns = patternEntities
        )
    }

    suspend fun deleteLearningStep(id: Long) {
        learningStepsDao.deleteLearningStep(id)
    }

    suspend fun existsLearningStepByName(name: String, excludedId: Long): Boolean {
        return learningStepsDao.existsLearningStepByName(
            name = name,
            excludedId = excludedId
        )
    }

    suspend fun copyLearningStep(id: Long): Long {
        val source = learningStepsDao.getLearningStepWithPatterns(id) ?: return 0L

        val newLearningStep = source.learningStep.copy(
            id = 0L,
            name = "${source.learningStep.name} - kopia",
            isActive = false
        )

        val newLearningStepId = learningStepsDao.insertLearningStep(newLearningStep)

        val copiedPatterns = source.patterns.map {
            it.copy(
                id = 0L,
                learningStepId = newLearningStepId
            )
        }

        if (copiedPatterns.isNotEmpty()) {
            learningStepsDao.insertLearningStepPatterns(copiedPatterns)
        }

        return newLearningStepId
    }
}
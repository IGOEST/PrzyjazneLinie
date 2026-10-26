package com.example.friendlylines.therapist_app.ui.configuration.summary

import androidx.lifecycle.ViewModel
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsState
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.LearningStepsReinforcementsDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LearningStepsSummaryScreenViewModel @Inject constructor() : ViewModel() {
    fun buildSummary(
        state: LearningStepsSettingsState,
        strings: SummaryStrings
    ): LearningStepsSummaryState {
        val patterns = state.patternState
        val learning = state.learningState
        val test = state.testState
        val reinforcements = state.reinforcementsState

        val enabledPatterns = patterns.patterns.filter { it.isEnabled }
        val learningPatterns = enabledPatterns.filter { it.isLearning }
        val testPatterns = enabledPatterns.filter { it.isTest }

        fun patternNames(patterns: List<LearningStepsPatternConfigDraft>): String {
            return patterns.joinToString(", ") { it.pattern.pattern.name }
        }

        fun yesNo(value: Boolean) = if (value) strings.yes else strings.no

        val rows = listOf(
            SummaryRow(
                label = strings.learnedPattersCount,
                learningValue = learningPatterns.size.toString(),
                testValue = testPatterns.size.toString()
            ),
            SummaryRow(
                label = strings.learnedPatterns,
                learningValue = patternNames(learningPatterns).ifBlank { strings.none },
                testValue = patternNames(testPatterns).ifBlank { strings.none }
            ),
            SummaryRow(
                label = strings.repetitions,
                learningValue = learning.repetitions.toString(),
                testValue = test.repetitions.toString()
            ),
            SummaryRow(
                label = strings.attempts,
                learningValue = learning.attempts.toString(),
                testValue = test.attempts.toString()
            ),
            SummaryRow(
                label = strings.timeLimit,
                learningValue = "${learning.timeLimit} s",
                testValue = "${test.timeLimit} s"
            ),
            SummaryRow(
                label = strings.accuracyLevel,
                learningValue = learning.accuracyLevel.name,
                testValue = test.accuracyLevel.name
            ),
            SummaryRow(
                label = strings.startingPointEnabled,
                learningValue = yesNo(learning.startingPointEnabled),
                testValue = yesNo(test.startingPointEnabled)
            ),
            SummaryRow(
                label = strings.randomPatternOrderEnabled,
                learningValue = yesNo(learning.randomPatternOrder),
                testValue = yesNo(test.randomPatternOrder)
            ),
            SummaryRow(
                label = strings.praiseWordsLabel,
                learningValue = if (reinforcements.praiseWordsEnabled) {
                    LearningStepsReinforcementsDraft.PRAISE_WORDS.filter { it in reinforcements.enabledPraiseWords }
                        .joinToString(", ") { strings.praiseWords.getValue(it) }
                    } else {
                        strings.no
                    },
                testValue = strings.notApplicable
            ),
            SummaryRow(
                label = strings.animationLabel,
                learningValue = if (reinforcements.animationsEnabled) {
                    LearningStepsReinforcementsDraft.ANIMATION_THEMES.filter { it in reinforcements.enabledAnimationThemes }
                        .joinToString(", ") { strings.animationThemes.getValue(it) }
                } else {
                    strings.no
                },
                testValue = strings.notApplicable
            ),
            SummaryRow(
                label = strings.endAnimation,
                learningValue = yesNo(reinforcements.endSessionAnimationEnabled),
                testValue = strings.notApplicable
            ),
            SummaryRow(
                label = strings.endFanfare,
                learningValue = yesNo(reinforcements.endSessionFanfareEnabled),
                testValue = strings.notApplicable
            )
        )

        return LearningStepsSummaryState(rows)
    }
}

data class SummaryStrings(
    val learnedPattersCount: String,
    val learnedPatterns: String,
    val repetitions: String,
    val attempts: String,
    val timeLimit: String,
    val accuracyLevel: String,
    val startingPointEnabled: String,
    val randomPatternOrderEnabled: String,
    val praiseWordsLabel: String,
    val praiseWords: Map<String, String>,
    val animationLabel: String,
    val animationThemes: Map<String, String>,
    val endAnimation: String,
    val endFanfare: String,
    val yes: String,
    val no: String,
    val none: String,
    val notApplicable: String
)
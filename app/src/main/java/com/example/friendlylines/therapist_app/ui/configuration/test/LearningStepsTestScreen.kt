package com.example.friendlylines.therapist_app.ui.configuration.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.ui.configuration.componenets.InfoBanner
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsTestDraft

private val CONTENT_PADDING = 20.dp
private val OVERRIDING_ALPHA = 1f
private val NOT_OVERRIDING_ALPHA = 0.5f

@Composable
fun LearningStepsTestScreen(
    onNextClick: () -> Unit,
    onEvent: (LearningStepsTestEvent) -> Unit,
    draft: LearningStepsTestDraft
) {
    val repetitionValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val attemptValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val timeLimitValues = listOf(10, 20, 30, 40, 50, 60)
    val accuracyValues = listOf(
        stringResource(R.string.easy_label_text),
        stringResource(R.string.medium_label_text),
        stringResource(R.string.hard_label_text)
    )

    val repetitions = draft.repetitions
    val attempts = draft.attempts
    val timeLimit = draft.timeLimit
    val accuracy = draft.accuracyLevel
    val randomPatternOrder = draft.randomPatternOrder
    val startingPoint = draft.startingPointEnabled

    var showOverrideHint by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CONTENT_PADDING.scaled())
    ) {
        PlainBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp.scaled()),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TemplateToggleSwitch(
                        checked = draft.overridesLearning,
                        onCheckedChange = { override ->
                            onEvent(
                                LearningStepsTestEvent.OverrideLearning(override)
                            )
                        }
                    )
                    Text(
                        text = stringResource(R.string.learning_steps_test_override_label),
                        style = FriendlyLinesTextStyles.headingH5Medium,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                    )
                    InfoIconButton(
                        infoTitle = stringResource(R.string.info_dialog_title),
                        infoMessage = stringResource(R.string.learning_steps_test_override_info_message),
                        modifier = Modifier.padding(start = 8.dp.scaled()),
                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                        disabledTint = FriendlyLinesColors.Neutral.N300
                    )
                }
                Row(modifier = Modifier.fillMaxSize().padding(CONTENT_PADDING)) {
                    val trialScrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = CONTENT_PADDING.scaled())
                            .alpha(if (draft.overridesLearning) OVERRIDING_ALPHA else NOT_OVERRIDING_ALPHA)
                            .clickable(enabled = !draft.overridesLearning) { showOverrideHint = true }
                            .verticalScroll(trialScrollState),
                        verticalArrangement = Arrangement.spacedBy(32.dp.scaled()),
                    ) {
                        TemplateSlider(
                            enabled = draft.overridesLearning,
                            showHeader = true,
                            showInfoIcon = true,
                            headerText = stringResource(R.string.num_of_repetitions),
                            infoTitle = stringResource(R.string.num_of_repetitions),
                            infoMessage = stringResource(R.string.num_of_repetitions_info),
                            values = repetitionValues,
                            selectedIndex = repetitionValues.indexOf(repetitions),
                            value = repetitions,
                            minValue = repetitionValues.first(),
                            maxValue = repetitionValues.last(),
                            onValueSelected = { index ->
                                onEvent(
                                    LearningStepsTestEvent.SetRepetitions(
                                        repetitionValues[index]
                                    )
                                )
                            },
                            showMinusPlus = true,
                            onMinusClick = {
                                onEvent(
                                    LearningStepsTestEvent.SetRepetitions(
                                        (repetitions - 1)
                                            .coerceAtLeast(repetitionValues.first())
                                    )
                                )
                            },
                            onPlusClick = {
                                onEvent(
                                    LearningStepsTestEvent.SetRepetitions(
                                        (repetitions + 1)
                                            .coerceAtMost(repetitionValues.last())
                                    )
                                )
                            }
                        )

                        TemplateSlider(
                            enabled = false,
                            showHeader = true,
                            showInfoIcon = true,
                            infoTitle = stringResource(R.string.num_of_attempts),
                            headerText = stringResource(R.string.num_of_attempts),
                            infoMessage = stringResource(R.string.num_of_attempts_info),
                            values = attemptValues,
                            selectedIndex = attemptValues.indexOf(draft.attempts),
                            value = draft.attempts,
                            minValue = attemptValues.first(),
                            maxValue = attemptValues.last(),
                            onValueSelected = {},
                            showMinusPlus = true,
                            onMinusClick = {},
                            onPlusClick = {}
                        )

                        TemplateSlider(
                            enabled = draft.overridesLearning,
                            showHeader = true,
                            showInfoIcon = true,
                            infoTitle = stringResource(R.string.time_limit),
                            headerText = stringResource(R.string.time_limit) + " (${draft.timeLimit} s)",
                            infoMessage = stringResource(R.string.time_limit_info),
                            values = timeLimitValues,
                            selectedIndex = timeLimitValues.indexOf(timeLimit),
                            value = timeLimit,
                            minValue = timeLimitValues.first(),
                            maxValue = timeLimitValues.last(),
                            onValueSelected = { index ->
                                onEvent(
                                    LearningStepsTestEvent.SetTimeLimit(
                                        timeLimitValues[index]
                                    )
                                )
                            },
                            showMinusPlus = true,
                            onMinusClick = {
                                onEvent(
                                    LearningStepsTestEvent.SetTimeLimit(
                                        (timeLimit - 1)
                                            .coerceAtLeast(timeLimitValues.first())
                                    )
                                )
                            },
                            onPlusClick = {
                                onEvent(
                                    LearningStepsTestEvent.SetTimeLimit(
                                        (timeLimit + 1)
                                            .coerceAtMost(timeLimitValues.last())
                                    )
                                )
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                InfoIconButton(
                                    infoTitle = stringResource(R.string.random_pattern_order),
                                    infoMessage = stringResource(R.string.random_pattern_order_info),
                                    modifier = Modifier.size(24.dp.scaled()),
                                    defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                                    activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                                    disabledTint = FriendlyLinesColors.Neutral.N300
                                )
                                Text(
                                    text = stringResource(R.string.random_pattern_order),
                                    style = FriendlyLinesTextStyles.headingH5Regular,
                                    color = FriendlyLinesColors.PrimaryFriendlyLines.P900
                                )
                            }
                            TemplateToggleSwitch(
                                checked = false,
                                onCheckedChange = {},
                                enabled = false
                            )
                        }

                        InfoBanner(
                            text = stringResource(R.string.learning_steps_test_random_order_banner_info),
                            modifier = Modifier.padding(top = 16.dp.scaled())
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp.scaled()),
                        thickness = 3.dp.scaled(),
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P700
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = CONTENT_PADDING.scaled())
                    ) {
                        val learningScrollState = rememberScrollState()
                        Column(
                            modifier =
                                Modifier.weight(1f)
                                    .alpha(if (draft.overridesLearning) OVERRIDING_ALPHA else NOT_OVERRIDING_ALPHA)
                                    .clickable(enabled = !draft.overridesLearning) { showOverrideHint = true }
                                    .verticalScroll(learningScrollState),
                            verticalArrangement = Arrangement.spacedBy(32.dp.scaled()),
                        ) {
                            TemplateSlider(
                                enabled = draft.overridesLearning,
                                showHeader = true,
                                showInfoIcon = true,
                                infoTitle = stringResource(R.string.level_of_accuracy),
                                headerText = stringResource(R.string.level_of_accuracy),
                                infoMessage = stringResource(R.string.level_of_accuracy_info),
                                values = accuracyValues,
                                value = accuracy.ordinal,
                                selectedIndex = accuracy.ordinal,
                                onValueSelected = { index ->
                                    onEvent(
                                        LearningStepsTestEvent.SetAccuracyLevel(
                                            AccuracyLevel.entries[index]
                                        )
                                    )
                                },
                                showMinusPlus = false
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    InfoIconButton(
                                        infoTitle = stringResource(R.string.starting_point),
                                        infoMessage = stringResource(R.string.starting_point_info),
                                        modifier = Modifier.size(24.dp.scaled()),
                                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                                        disabledTint = FriendlyLinesColors.Neutral.N300
                                    )
                                    Text(
                                        text = stringResource(R.string.starting_point),
                                        style = FriendlyLinesTextStyles.headingH5Regular,
                                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900
                                    )
                                }
                                TemplateToggleSwitch(
                                    checked = false,
                                    onCheckedChange = {},
                                    enabled = false
                                )
                            }

                            InfoBanner(
                                text = stringResource(R.string.learning_steps_test_no_hints_banner_info),
                                modifier = Modifier.padding(top = 16.dp.scaled())
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TemplateButton(
                                enabled = true,
                                onClick = onNextClick,
                                text = stringResource(R.string.next_button_text),
                                icon = Icons.Filled.ArrowForwardIos
                            )
                        }
                    }
                }
            }
        }
    }

    if (showOverrideHint) {
        TemplateInfoDialog(
            title = stringResource(R.string.info_dialog_title),
            message = stringResource(R.string.learning_steps_test_override_hint_message),
            onDismiss = { showOverrideHint = false },
        )
    }
}
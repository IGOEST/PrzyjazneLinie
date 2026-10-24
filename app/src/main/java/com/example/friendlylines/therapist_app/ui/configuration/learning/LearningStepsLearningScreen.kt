package com.example.friendlylines.therapist_app.ui.configuration.learning

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.shared.data.drafts.AccuracyLevel
import com.example.shared.data.drafts.LearningStepsLearningDraft

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepsLearningScreen(
    onNextClick: () -> Unit,
    onEvent: (LearningStepsLearningEvent) -> Unit,
    draft: LearningStepsLearningDraft
) {
    val repetitionValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val attemptValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val timeLimitValues = listOf(10, 20, 30, 40, 50, 60)
    val accuracyValues = listOf(
        stringResource(R.string.easy_label_text),
        stringResource(R.string.medium_label_text),
        stringResource(R.string.hard_label_text)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CONTENT_PADDING.scaled())
    ) {
        PlainBackground() {
            Row(modifier = Modifier.fillMaxSize().padding(CONTENT_PADDING)) {
                val trialScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = CONTENT_PADDING.scaled())
                        .verticalScroll(trialScrollState),
                    verticalArrangement = Arrangement.spacedBy(32.dp.scaled()),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.learning_trial_settings_header),
                            style = FriendlyLinesTextStyles.headingH5Medium,
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                            textAlign = TextAlign.Center,
                        )
                        InfoIconButton(
                            infoTitle = stringResource(R.string.info_dialog_title),
                            infoMessage = stringResource(R.string.learning_trial_setting_info_message),
                            modifier = Modifier.padding(start = 8.dp.scaled()),
                            defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                            activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                            disabledTint = FriendlyLinesColors.Neutral.N300
                        )
                    }

                    TemplateSlider(
                        showHeader = true,
                        showInfoIcon = true,
                        headerText = stringResource(R.string.num_of_repetitions),
                        infoTitle = stringResource(R.string.num_of_repetitions),
                        infoMessage = stringResource(R.string.num_of_repetitions_info),
                        values = repetitionValues,
                        selectedIndex = repetitionValues.indexOf(draft.repetitions),
                        value = draft.repetitions,
                        minValue = repetitionValues.first(),
                        maxValue = repetitionValues.last(),
                        onValueSelected = { index ->
                            onEvent(
                                LearningStepsLearningEvent.SetRepetitions(
                                    repetitionValues[index]
                                )
                            )
                        },
                        showMinusPlus = true,
                        onMinusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetRepetitions(
                                    (draft.repetitions - 1)
                                        .coerceAtLeast(repetitionValues.first())
                                )
                            )
                        },
                        onPlusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetRepetitions(
                                    (draft.repetitions + 1)
                                        .coerceAtMost(repetitionValues.last())
                                )
                            )
                        }
                    )

                    TemplateSlider(
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
                        onValueSelected = { index ->
                            onEvent(
                                LearningStepsLearningEvent.SetAttempts(
                                    attemptValues[index]
                                )
                            )
                        },
                        showMinusPlus = true,
                        onMinusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetAttempts(
                                    (draft.attempts - 1)
                                        .coerceAtLeast(attemptValues.first())
                                )
                            )
                        },
                        onPlusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetAttempts(
                                    (draft.attempts + 1)
                                        .coerceAtMost(attemptValues.last())
                                )
                            )
                        }
                    )

                    TemplateSlider(
                        showHeader = true,
                        showInfoIcon = true,
                        infoTitle = stringResource(R.string.time_limit),
                        headerText = stringResource(R.string.time_limit) + " (${draft.timeLimit} s)",
                        infoMessage = stringResource(R.string.time_limit_info),
                        values = timeLimitValues,
                        selectedIndex = timeLimitValues.indexOf(draft.timeLimit),
                        value = draft.timeLimit,
                        minValue = timeLimitValues.first(),
                        maxValue = timeLimitValues.last(),
                        onValueSelected = { index ->
                            onEvent(
                                LearningStepsLearningEvent.SetTimeLimit(
                                    timeLimitValues[index]
                                )
                            )
                        },
                        showMinusPlus = true,
                        onMinusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetTimeLimit(
                                    (draft.timeLimit - 1)
                                        .coerceAtLeast(timeLimitValues.first())
                                )
                            )
                        },
                        onPlusClick = {
                            onEvent(
                                LearningStepsLearningEvent.SetTimeLimit(
                                    (draft.timeLimit + 1)
                                        .coerceAtMost(timeLimitValues.last())
                                )
                            )
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                            checked = draft.randomPatternOrder,
                            onCheckedChange = { enabled ->
                                onEvent(
                                    LearningStepsLearningEvent.SetRandomPatternOrder(
                                        enabled
                                    )
                                )
                            }
                        )
                    }
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
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = CONTENT_PADDING.scaled())
                            .verticalScroll(learningScrollState),
                        verticalArrangement = Arrangement.spacedBy(32.dp.scaled()),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.learning_learning_settings_header),
                                style = FriendlyLinesTextStyles.headingH5Medium,
                                color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                                textAlign = TextAlign.Center,
                            )
                            InfoIconButton(
                                infoTitle = stringResource(R.string.info_dialog_title),
                                infoMessage = stringResource(R.string.learning_learning_settings_info_message),
                                modifier = Modifier.padding(start = 8.dp.scaled()),
                                defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                                activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                                disabledTint = FriendlyLinesColors.Neutral.N300
                            )
                        }

                        TemplateSlider(
                            showHeader = true,
                            showInfoIcon = true,
                            infoTitle = stringResource(R.string.level_of_accuracy),
                            headerText = stringResource(R.string.level_of_accuracy),
                            infoMessage = stringResource(R.string.level_of_accuracy_info),
                            values = accuracyValues,
                            selectedIndex = draft.accuracyLevel.ordinal,
                            onValueSelected = { index ->
                                onEvent(
                                    LearningStepsLearningEvent.SetAccuracyLevel(
                                        AccuracyLevel.entries[index]
                                    )
                                )
                            },
                            showMinusPlus = false
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
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
                                checked = draft.startingPointEnabled,
                                onCheckedChange = { enabled ->
                                    onEvent(
                                        LearningStepsLearningEvent.SetStartingPointEnabled(
                                            enabled
                                        )
                                    )
                                }
                            )
                        }
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
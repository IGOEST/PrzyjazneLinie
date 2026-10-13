package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsState
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

@Composable
fun LearningStepSummaryBox(
    state: LearningStepsSettingsState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    val enabledPatterns = state.patternState.patterns.filter { it.isEnabled }

    val patternNames = enabledPatterns.joinToString(", ") { it.pattern.pattern.name }

    Box(
        modifier = modifier
    ) {
        ScrollArea(
            state = scrollAreaState,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(
                            width = 2.dp,
                            color = Primary900,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .padding(4.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item { SummaryHeaderRow() }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.learned_patterns_number),
                                learningValue = enabledPatterns.size.toString(),
                                testValue = enabledPatterns.size.toString()
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.learned_patterns),
                                learningValue = patternNames.ifBlank { stringResource(R.string.none) },
                                testValue = patternNames.ifBlank { stringResource(R.string.none)  }
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.num_of_repetitions),
                                learningValue = state.learningState.repetitions.toString(),
                                testValue = state.testState.repetitions.toString()
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.num_of_attempts),
                                learningValue = state.learningState.attempts.toString(),
                                testValue = state.testState.attempts.toString()
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.time_limit),
                                learningValue = "${state.learningState.timeLimit} s",
                                testValue = "${state.testState.timeLimit} s"
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.level_of_accuracy),
                                learningValue = state.learningState.accuracyLevel.toString(),
                                testValue = state.testState.accuracyLevel.toString()
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.starting_point),
                                learningValue = state.learningState.startingPointEnabled.toYesNo(),
                                testValue = state.testState.startingPointEnabled.toYesNo()
                            )
                        }

                        item {
                            SummaryRow(
                                label = stringResource(R.string.random_pattern_order),
                                learningValue = state.learningState.randomPatternOrder.toYesNo(),
                                testValue = state.testState.randomPatternOrder.toYesNo()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                VerticalScrollbar(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                ) {
                    Thumb(
                        modifier = Modifier
                            .background(
                                color = Primary700,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryCell(
            text = stringResource(R.string.step_info),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp
        )

        SummaryCell(
            text = stringResource(R.string.learning_mode_summary),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        SummaryCell(
            text = stringResource(R.string.test_mode_summary),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    learningValue: String,
    testValue: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Primary1000,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryCell(
            text = label,
            modifier = Modifier.weight(1f),
            fontSize = 20.sp
        )

        SummaryCell(
            text = learningValue,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        SummaryCell(
            text = testValue,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun SummaryCell(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 8.dp),
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        color = Primary1000
    )
}

@Composable
private fun Boolean.toYesNo(): String {
    return if (this) stringResource(R.string.yes) else stringResource(R.string.no)
}
package com.example.friendlylines.therapist_app.ui.configuration.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.components.NameError
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsState
import com.example.friendlylines.therapist_app.ui.configuration.summary.components.LearningStepSummaryBox

private val CONTENT_PADDING = 20.dp
private val SAVE_CONTENT_WIDTH = 350.dp

@Composable
fun LearningStepsSummaryScreen(
    onSaveClick: () -> Unit,
    state: LearningStepsSettingsState,
    onEvent: (LearningStepsSummaryEvent) -> Unit,
    viewModel: LearningStepsSummaryScreenViewModel = hiltViewModel()
) {
    val strings = SummaryStrings(
        learnedPattersCount = stringResource(R.string.learned_patterns_number),
        learnedPatterns = stringResource(R.string.learned_patterns),
        repetitions = stringResource(R.string.num_of_repetitions),
        attempts = stringResource(R.string.num_of_attempts),
        timeLimit = stringResource(R.string.time_limit),
        accuracyLevel = stringResource(R.string.level_of_accuracy),
        startingPointEnabled = stringResource(R.string.starting_point),
        randomPatternOrderEnabled = stringResource(R.string.random_pattern_order),
        yes = stringResource(R.string.yes),
        no = stringResource(R.string.no),
        none = stringResource(R.string.none)
    )

    val summaryTable = viewModel.buildSummary(state, strings)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PlainBackground(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CONTENT_PADDING.scaled())
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = CONTENT_PADDING.scaled())
                ) {
                    LearningStepSummaryBox(
                        state = summaryTable
                    )
                }

                Column(
                    modifier = Modifier
                        .width(SAVE_CONTENT_WIDTH.scaled())
                        .align(Alignment.CenterVertically)
                        .padding(start = CONTENT_PADDING.scaled()),
                    verticalArrangement = Arrangement.spacedBy(16.dp.scaled())
                ) {
                    OutlinedTextField(
                        value = state.stepName,
                        onValueChange = {
                            onEvent(
                                LearningStepsSummaryEvent.NameChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(R.string.enter_step_name) + ":",
                                style = FriendlyLinesTextStyles.captionC1
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                            unfocusedBorderColor = FriendlyLinesColors.Neutral.N300,
                            focusedLabelColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                            unfocusedLabelColor = FriendlyLinesColors.Neutral.N300,
                            errorBorderColor = FriendlyLinesColors.States.Error700,
                            errorLabelColor = FriendlyLinesColors.States.Error700,
                            cursorColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                        ),
                        singleLine = true,
                        isError = state.stepNameError != null,
                        supportingText = {
                            state.stepNameError?.let { error ->
                                Text(
                                    text = when (error) {
                                        NameError.BLANK -> stringResource(R.string.name_blank_error)
                                        NameError.EXISTS -> stringResource(R.string.name_exists_error)
                                    },
                                    color = FriendlyLinesColors.States.Error700,
                                    style = FriendlyLinesTextStyles.captionC1,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 4.dp.scaled()
                                        ),
                                    textAlign = TextAlign.Left
                                )
                            }
                        }
                    )

                    TemplateButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.stepName.isNotBlank(),
                        onClick = onSaveClick,
                        text = stringResource(R.string.save_button_text),
                        icon = Icons.Filled.Save
                    )
                }
            }
        }
    }
}
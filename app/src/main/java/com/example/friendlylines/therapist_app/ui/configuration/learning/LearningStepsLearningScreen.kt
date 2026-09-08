package com.example.friendlylines.therapist_app.ui.configuration.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shared.data.drafts.AccuracyLevel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsViewModel

@Composable
fun LearningStepsLearningScreen(
    onNextClick: () -> Unit,
    settingsViewModel: LearningStepsSettingsViewModel
) {
    val learningRepetitions by settingsViewModel.learningRepetitions.collectAsStateWithLifecycle()
    val learningAttempts by settingsViewModel.learningAttempts.collectAsStateWithLifecycle()
    val learningTimeLimit by settingsViewModel.learningTimeLimit.collectAsStateWithLifecycle()
    val learningAccuracy by settingsViewModel.learningAccuracy.collectAsStateWithLifecycle()
    val startingPointEnabled by settingsViewModel.startingPointEnabled.collectAsStateWithLifecycle()
    val randomPatternOrder by settingsViewModel.randomPatternOrder.collectAsStateWithLifecycle()

    // SLIDER VALUES
    val repetitionValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val attemptValues = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val timeLimitValues = listOf(10, 20, 30, 40, 50, 60)
    val accuracyValues = listOf(
        stringResource(R.string.easy_label_text),
        stringResource(R.string.medium_label_text),
        stringResource(R.string.hard_label_text)
    )

    // STATES - information dialog
    var showRepetitionsInfo by remember {
        mutableStateOf(false)
    }

    var showAttemptsInfo by remember {
        mutableStateOf(false)
    }

    var showTimeLimitInfo by remember {
        mutableStateOf(false)
    }

    var showAccuracyInfo by remember {
        mutableStateOf(false)
    }

    var showStartingPointInfo by remember {
        mutableStateOf(false)
    }

    var showRandomOrderInfo by remember {
        mutableStateOf(false)
    }

    // MAIN SCREEN LAYOUT
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
    ) {

        // NUMBER OF REPETITIONS
        LearningSettingRow(
            title = stringResource(R.string.num_of_repetitions),
            onInfoClick = {
                showRepetitionsInfo = true
            }
        ) {
            TemplateSlider(
                values = repetitionValues,
                selectedIndex = repetitionValues.indexOf(learningRepetitions),
                onValueSelected = { index ->
                    settingsViewModel.setLearningRepetitions(repetitionValues[index])
                },
                showMinusPlus = true,
                onMinusClick = {
                    settingsViewModel.setLearningRepetitions(
                        (learningRepetitions - 1)
                            .coerceAtLeast(repetitionValues.first())
                    )
                },
                onPlusClick = {
                    settingsViewModel.setLearningRepetitions(
                        (learningRepetitions + 1)
                            .coerceAtMost(repetitionValues.last())
                    )
                },
                sliderWidth = 320.dp
            )
        }

        // NUMBER OF ATTEMPTS
        LearningSettingRow(
            title = stringResource(R.string.num_of_attempts),
            onInfoClick = {
                showAttemptsInfo = true
            }
        ) {
            TemplateSlider(
                values = attemptValues,
                selectedIndex = attemptValues.indexOf(learningAttempts),
                onValueSelected = { index ->
                    settingsViewModel.setLearningAttempts(attemptValues[index])
                },
                showMinusPlus = true,
                onMinusClick = {
                    settingsViewModel.setLearningAttempts(
                        (learningAttempts - 1)
                            .coerceAtLeast(attemptValues.first())
                    )
                },
                onPlusClick = {
                    settingsViewModel.setLearningAttempts(
                        (learningAttempts + 1)
                            .coerceAtMost(attemptValues.last())
                    )
                },
                sliderWidth = 320.dp
            )
        }

        // TIME LIMIT
        LearningSettingRow(
            title = stringResource(R.string.time_limit),
            onInfoClick = {
                showTimeLimitInfo = true
            }
        ) {
            TemplateSlider(
                values = timeLimitValues,
                selectedIndex = timeLimitValues.indexOf(learningTimeLimit),
                onValueSelected = { index ->
                    settingsViewModel.setLearningTimeLimit(timeLimitValues[index])
                },
                showMinusPlus = true,
                onMinusClick = {
                    val currentIndex = timeLimitValues.indexOf(learningTimeLimit)

                    if (currentIndex > 0) {
                        settingsViewModel.setLearningTimeLimit(timeLimitValues[currentIndex - 1])
                    }
                },
                onPlusClick = {
                    val currentIndex = timeLimitValues.indexOf(learningTimeLimit)

                    if (currentIndex < timeLimitValues.lastIndex) {
                        settingsViewModel.setLearningTimeLimit(timeLimitValues[currentIndex + 1])
                    }
                },
                sliderWidth = 320.dp
            )
        }

        // ACCURACY LEVEL
        LearningSettingRow(
            title = stringResource(R.string.level_of_accuracy),
            onInfoClick = {
                showAccuracyInfo = true
            }
        ) {
            TemplateSlider(
                values = accuracyValues,
                selectedIndex = learningAccuracy.ordinal,
                onValueSelected = { index ->
                    settingsViewModel.setLearningAccuracy(
                        AccuracyLevel.entries[index]
                    )
                },
                showMinusPlus = false,
                sliderWidth = 250.dp
            )
        }

        // STARTING POINT
        LearningSettingRow(
            title = stringResource(R.string.starting_point),
            onInfoClick = {
                showStartingPointInfo = true
            }
        ) {
            TemplateToggleSwitch(
                checked = startingPointEnabled,
                onCheckedChange = { enabled ->
                    settingsViewModel.setStartingPointEnabled(enabled)
                },
                modifier = Modifier.size(
                    width = 52.dp,
                    height = 32.dp
                )
            )
        }

        // RANDOM PATTERN ORDER
        LearningSettingRow(
            title = stringResource(R.string.random_pattern_order),
            onInfoClick = {
                showRandomOrderInfo = true
            }
        ) {
            TemplateToggleSwitch(
                checked = randomPatternOrder,
                onCheckedChange = { enabled ->
                    settingsViewModel.setRandomPatternOrder(enabled)
                },
                modifier = Modifier.size(
                    width = 52.dp,
                    height = 32.dp
                )
            )
        }

        // SPACE ABOVE THE NEXT BUTTON (that should be at the bottom of the screen)
        Spacer(
            modifier = Modifier.weight(1f)
        )

        // NEXT BUTTON (aligned to the right side of the screen)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TemplateButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(54.dp),
                enabled = true,
                onClick = onNextClick,
                text = stringResource(R.string.next_button_text),
                icon = Icons.Filled.ArrowForwardIos
            )
        }
    }

    // INFORMATION DIALOGS (displayed only when their corresponding boolean is true)
    if (showRepetitionsInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.num_of_repetitions),
            info = stringResource(R.string.num_of_repetitions_info),
            onDismiss = {
                showRepetitionsInfo = false
            }
        )
    }

    if (showAttemptsInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.num_of_attempts),
            info = stringResource(R.string.num_of_attempts_info),
            onDismiss = {
                showAttemptsInfo = false
            }
        )
    }

    if (showTimeLimitInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.time_limit),
            info = stringResource(R.string.time_limit_info),
            onDismiss = {
                showTimeLimitInfo = false
            }
        )
    }

    if (showAccuracyInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.level_of_accuracy),
            info = stringResource(R.string.level_of_accuracy_info),
            onDismiss = {
                showAccuracyInfo = false
            }
        )
    }

    if (showStartingPointInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.starting_point),
            info = stringResource(R.string.starting_point_info),
            onDismiss = {
                showStartingPointInfo = false
            }
        )
    }

    if (showRandomOrderInfo) {
        TemplateInfoDialog(
            title = stringResource(R.string.random_pattern_order),
            info = stringResource(R.string.random_pattern_order_info),
            onDismiss = {
                showRandomOrderInfo = false
            }
        )
    }
}

// ROW STRUCTURE
@Composable
fun LearningSettingRow(
    title: String,
    onInfoClick: () -> Unit,
    settingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // INFORMATION ICON
        TemplateClickableIcon(
            icon = Icons.Default.Info,
            contentDescription = null,
            onClick = onInfoClick,
            modifier = Modifier.size(32.dp),
            defaultTint = InfoDefault,
            activeTint = InfoActive,
            disabledTint = Neutral300
        )

        // SPACE (between the information icon and the title)
        Spacer(
            modifier = Modifier.width(12.dp)
        )

        // SETTING TITLE
        Text(
            text = title,
            fontSize = 24.sp
        )

        // SPACE (between title and setting component)
        Spacer(
            modifier = Modifier.weight(1f)
        )

        // SETTING COMPONENT
        settingContent()
    }
}
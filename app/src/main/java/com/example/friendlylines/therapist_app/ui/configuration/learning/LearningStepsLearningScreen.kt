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
import androidx.compose.material.icons.outlined.ArrowForwardIos
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


@Composable
fun LearningStepsLearningScreen(
    onNextClick: () -> Unit
) {

    // STATES - sliders and switches
    var repetitions by remember {
        mutableStateOf(3)
    }

    var attempts by remember {
        mutableStateOf(3)
    }

    var timeLimit by remember {
        mutableStateOf(15)
    }

    var accuracy by remember {
        mutableStateOf(1) // 0 = easy, 1 = medium, 2 = hard
    }

    var startingPointEnabled by remember {
        mutableStateOf(false)
    }

    var randomPatternOrder by remember {
        mutableStateOf(false)
    }


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
            .padding(32.dp)
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
                selectedIndex = repetitionValues.indexOf(repetitions),
                onValueSelected = { index ->
                    repetitions = repetitionValues[index]
                },
                showMinusPlus = true,
                onMinusClick = {    // decreases the value by one step
                    repetitions = (repetitions - 1)
                        .coerceAtLeast(repetitionValues.first())    // prevents the value from going below the smallest available value
                },
                onPlusClick = {     // increase the value by one step
                    repetitions = (repetitions + 1)
                        .coerceAtMost(repetitionValues.last())    // prevents the value from going above the largest available value
                },
                sliderWidth = 250.dp
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
                selectedIndex = attemptValues.indexOf(attempts),
                onValueSelected = { index ->
                    attempts = attemptValues[index]
                },
                showMinusPlus = true,
                onMinusClick = {
                    attempts = (attempts - 1)
                        .coerceAtLeast(attemptValues.first())
                },
                onPlusClick = {
                    attempts = (attempts + 1)
                        .coerceAtMost(attemptValues.last())
                },
                sliderWidth = 250.dp
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
                selectedIndex = timeLimitValues.indexOf(timeLimit),
                onValueSelected = { index ->
                    timeLimit = timeLimitValues[index]
                },
                showMinusPlus = true,
                onMinusClick = {
                    val currentIndex = timeLimitValues.indexOf(timeLimit)

                    if (currentIndex > 0) {
                        timeLimit = timeLimitValues[currentIndex - 1]
                    }
                },
                onPlusClick = {
                    val currentIndex = timeLimitValues.indexOf(timeLimit)

                    if (currentIndex < timeLimitValues.lastIndex) {
                        timeLimit = timeLimitValues[currentIndex + 1]
                    }
                },
                sliderWidth = 250.dp
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
                selectedIndex = accuracy,
                onValueSelected = { index ->
                    accuracy = index
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
                    startingPointEnabled = enabled
                },
                modifier = Modifier.size(
                    width = 38.dp,
                    height = 24.dp
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
                    randomPatternOrder = enabled
                },
                modifier = Modifier.size(
                    width = 38.dp,
                    height = 24.dp
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
                icon = Icons.Outlined.ArrowForwardIos
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
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // INFORMATION ICON
        TemplateClickableIcon(
            icon = Icons.Default.Info,
            contentDescription = null,
            onClick = onInfoClick,
            modifier = Modifier.size(24.dp),
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
            text = title
        )

        // SPACE (between title and setting component)
        Spacer(
            modifier = Modifier.weight(1f)
        )

        // SETTING COMPONENT
        settingContent()
    }
}
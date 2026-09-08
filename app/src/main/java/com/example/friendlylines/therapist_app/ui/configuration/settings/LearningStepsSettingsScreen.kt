package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreenViewModel
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestScreenViewModel
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateTabBar
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreen
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreen
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryScreen
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestScreen
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.friendlylines.therapist_app.ui.main.NavRoutes
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary900

enum class LearningStepTab(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    PATTERNS(
        title = R.string.patterns_tab_text,
        icon = Icons.Default.Photo
    ),
    LEARNING(
        title = R.string.learning_tab_text,
        icon = Icons.Default.Settings
    ),
    TEST(
        title = R.string.test_tab_text,
        icon = Icons.Default.School
    ),
    REINFORCEMENTS(
        title = R.string.reinforcements_tab_text,
        icon = Icons.Default.ThumbUp
    ),
    SUMMARY(
        title = R.string.summary_tab_text,
        icon = Icons.Default.ListAlt
    )
}

@Composable
fun LearningStepsSettingsScreen(
    navController: NavController,
    stepId: Long?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    settingsViewModel: LearningStepsSettingsViewModel = hiltViewModel()
) {
    var selectedTab by remember {
        mutableStateOf(LearningStepTab.PATTERNS)
    }

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    var exitDestination by remember {
        mutableStateOf<ExitDestination?>(null)
    }

    BackHandler {
        showExitDialog = true
        exitDestination = ExitDestination.PREVIOUS
    }

    Scaffold(
        containerColor = Primary50,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary900)
                    .statusBarsPadding()
            ) {
                TemplateTopAppBar(
                    //TODO: Wyświetlanie nazwy konfiguracji kroku uczenia przy edycji
                    text = stringResource(R.string.new_learning_step_header),
                    onBackClick = {
                        showExitDialog = true
                        exitDestination = ExitDestination.PREVIOUS
                    },
                    onHomeClick = {
                        showExitDialog = true
                        exitDestination = ExitDestination.HOME
                    }
                )

                TemplateTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = {
                        selectedTab = it
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                LearningStepTab.PATTERNS -> {
                    LearningStepsPatternsScreen(
                        navController = navController,
                        stepId = stepId,
                        settingsViewModel = settingsViewModel,
                        onBackClick = onBackClick,
                        onHomeClick = onHomeClick,
                        onAddPatternClick = { order ->
                            navController.navigate(
                                NavRoutes.learningStepsPatternAdd(
                                    order = order
                                )
                            )
                        },
                        onNextClick = {
                            selectedTab = LearningStepTab.LEARNING
                        }
                    )
                }

                LearningStepTab.LEARNING -> {
                    LearningStepsLearningScreen(
                        settingsViewModel = settingsViewModel,
                        onNextClick = {
                            selectedTab = LearningStepTab.TEST
                        }
                    )
                }

                LearningStepTab.TEST -> {
                    LearningStepsTestScreen(
                        settingsViewModel = settingsViewModel,
                        onNextClick = {
                            selectedTab = LearningStepTab.SUMMARY
                        }
                    )
                }

                LearningStepTab.REINFORCEMENTS -> {
                    // później
                }

                LearningStepTab.SUMMARY -> {
                    LearningStepsSummaryScreen(
                        settingsViewModel = settingsViewModel,
                        onSaved = {
                            onBackClick()
                        }
                    )
                }

                else -> {}
            }
        }
    }

    if (showExitDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                showExitDialog = false
                when (exitDestination) {
                    ExitDestination.PREVIOUS -> {
                        onBackClick()
                    }
                    ExitDestination.HOME -> {
                        onHomeClick()
                    }
                    null -> Unit
                }
                exitDestination = null
            },
            onDismiss = {
                showExitDialog = false
                exitDestination = null
            }
        )
    }
}
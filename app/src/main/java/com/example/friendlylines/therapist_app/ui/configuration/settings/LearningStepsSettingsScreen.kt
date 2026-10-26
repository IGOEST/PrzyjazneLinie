package com.example.friendlylines.therapist_app.ui.configuration.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.friendlylines.R
import com.example.friendlylines.components.LoadingScreen
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.therapist_app.backgrounds.MascotNarrowHelpBackground
import com.example.friendlylines.therapist_app.backgrounds.MascotWideHelpBackground
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.navigation.ExitDestination
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.navigation.TherapistNavRoutes
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.configuration.learning.LearningStepsLearningScreen
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreen
import com.example.friendlylines.therapist_app.ui.configuration.reinforcement.LearningStepsReinforcementScreen
import com.example.friendlylines.therapist_app.ui.configuration.settings.componenets.LearningStepTab
import com.example.friendlylines.therapist_app.ui.configuration.settings.componenets.TemplateTabBar
import com.example.friendlylines.therapist_app.ui.configuration.settings.componenets.tabPageInfo
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryEvent
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryScreen
import com.example.friendlylines.therapist_app.ui.configuration.test.LearningStepsTestScreen

@Composable
fun LearningStepsSettingsScreen(
    navController: NavController,
    stepId: Long?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    viewModel: LearningStepsSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.savedLearningStepId) {
        state.savedLearningStepId?.let { learningStepId ->

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("newLearningStepId", learningStepId)

            navController.popBackStack()

            viewModel.onEvent(
                LearningStepsSettingsEvent.SaveSuccessHandled
            )
        }
    }

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
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FriendlyLinesColors.PrimaryFriendlyLines.P900)
                    .statusBarsPadding()
            ) {
                TemplateTopAppBar(
                    text = if (stepId == null) {
                        stringResource(R.string.new_learning_step_header)
                    } else {
                        state.stepName
                    },
                    onBackClick = {
                        showExitDialog = true
                        exitDestination = ExitDestination.PREVIOUS
                    },
                    onHomeClick = {
                        showExitDialog = true
                        exitDestination = ExitDestination.HOME
                    },
                    infoTitle = stringResource(R.string.info_dialog_title),
                    infoMessage = stringResource(selectedTab.tabPageInfo())
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
        Column (
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            when (selectedTab) {
                LearningStepTab.PATTERNS -> {
                    val content: @Composable () -> Unit = {
                        LearningStepsPatternsScreen(
                            navController = navController,
                            stepId = stepId,
                            onBackClick = onBackClick,
                            onHomeClick = onHomeClick,
                            onAddPatternClick = { order ->
                                navController.navigate(
                                    TherapistNavRoutes.LearningStepsPatternAdd(
                                        stepId = stepId,
                                        order = order
                                    )
                                )
                            },
                            onNextClick = {
                                selectedTab = LearningStepTab.LEARNING
                            },
                            onEvent = { viewModel.onEvent(LearningStepsSettingsEvent.Patterns(it)) },
                            draft = state.patternState
                        )
                    }
                    if (state.patternState.patterns.isEmpty() && !state.isLoading) {
                        MascotWideHelpBackground(
                            helpText = stringResource(R.string.learning_steps_pattern_help)
                        ) {
                            content()
                        }
                    } else if (state.isLoading) {
                        PlainBackground { LoadingScreen() }
                    } else {
                        PlainBackground { content() }
                    }
                }

                LearningStepTab.LEARNING -> {
                    LearningStepsLearningScreen(
                        onNextClick = {
                            selectedTab = LearningStepTab.REINFORCEMENTS
                        },
                        onEvent = { viewModel.onEvent(LearningStepsSettingsEvent.Learning(it)) },
                        draft = state.learningState
                    )
                }

                LearningStepTab.REINFORCEMENTS -> {
                    LearningStepsReinforcementScreen(
                        onNextClick = {
                            selectedTab = LearningStepTab.TEST
                        },
                        onEvent = { viewModel.onEvent(LearningStepsSettingsEvent.Reinforcements(it))},
                        draft = state.reinforcementsState
                    )
                }

                LearningStepTab.TEST -> {
                    LearningStepsTestScreen(
                        onNextClick = {
                            selectedTab = LearningStepTab.SUMMARY
                        },
                        onEvent ={ viewModel.onEvent(LearningStepsSettingsEvent.Test(it)) },
                        draft = state.testState
                    )
                }

                LearningStepTab.SUMMARY -> {
                    LearningStepsSummaryScreen(
                        state = state,
                        onEvent = {
                            viewModel.onEvent(
                                LearningStepsSettingsEvent.Summary(it)
                            )
                        },
                        onSaveClick = {
                            viewModel.onEvent(
                                LearningStepsSettingsEvent.Summary(
                                    LearningStepsSummaryEvent.SaveClicked
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    if (state.showSaveDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.save_config_dialog_title),
            confirmText = stringResource(R.string.save_button_text),
            message = null,
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                viewModel.onEvent(
                    LearningStepsSettingsEvent.Summary(
                        LearningStepsSummaryEvent.SaveConfirmed
                    )
                )
            },
            onDismiss = {
                viewModel.onEvent(
                    LearningStepsSettingsEvent.Summary(
                        LearningStepsSummaryEvent.SaveDialogDismissed
                    )
                )
            }
        )
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